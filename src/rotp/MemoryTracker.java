package rotp;

import java.lang.management.GarbageCollectorMXBean;
import java.util.Arrays;
import java.util.List;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;

public class MemoryTracker {
	private static final int MB	  = 1048576;
	private static final int size = 10;
	private boolean isMemoryMonitored = false;
	private long maxUsedMemory;
	private long maxAllocatedMemory;
	private long gcReserve = -1;
	private long streamReserve = -1;
	private long[] lastReserves;
	private int index = 0;
	
	MemoryTracker(long allocated) {
		streamReserve = allocated;
		lastReserves  = new long[size];
		Arrays.fill(lastReserves, allocated);
		installGCMonitoring();
	}
	boolean memoryLow() {
		if (isMemoryMonitored)
			return gcReserve < 300;
		else {
			if (streamReserve == -1)
				getMemoryInfo();
			return streamReserve < 100;
		}
	}
	String getMemoryInfo(boolean screen) { return(getMemoryInfo(screen, false)); }
	private String getMemoryInfo()		 { return getMemoryInfo(false, true); }
	private String getMemoryInfo(boolean screen, boolean update) {
		// get info
		int  threads	= Thread.activeCount();
		long available	= Runtime.getRuntime().maxMemory()   / MB;
		long allocated	= Runtime.getRuntime().totalMemory() / MB;
		long free		= Runtime.getRuntime().freeMemory()  / MB;
		long used		= allocated - free;
		long reserves	= available - used;
		// isMemoryMonitored = true; // TO DO Comment
		// Update reserve values
		if (screen) { // Stream update
			lastReserves[index] = reserves;
			index++;
			if (index >= size)
				index = 0;
			streamReserve = Arrays.stream(lastReserves).min().getAsLong();
		}
		if (update && isMemoryMonitored)
			gcReserve	  = reserves;
		// Maximum validation
		if (used > maxUsedMemory)
			maxUsedMemory = used;
		if (allocated > maxAllocatedMemory)
			maxAllocatedMemory = allocated;
		
		if (screen) { // Return Display String
			String s;
			if (isMemoryMonitored)
				s = "[" + gcReserve  + "M] ";
			else
				s = "[-] ";
			s += "{" + streamReserve  + "M} " +
					used	  + "M / "  +
					allocated + "M / "  +
					available + "M  ("  +
					maxUsedMemory + "/" +
					maxAllocatedMemory  + ")";
			if (threads >= 15)
				s += " T:" + Integer.toString(threads);
			return s;
		}
		else { // Return console String
			String s = "Memory Reserve:";
			if (isMemoryMonitored)
				s = "[" + String.format("% 5d", gcReserve) + " MB]";
			else
				s = "[-]";
			s += " {" + String.format("% 5d", streamReserve) + " MB}" +
					" | Used:"			+ String.format("% 5d", used) + " MB" +
					" | Allocated:"		+ String.format("% 5d", allocated) + " MB" +
					" | Available:"		+ String.format("% 5d", available) + " MB" +
					" | Max used:"		+ String.format("% 5d", maxUsedMemory) + " MB" +
					" | Max allocated:"	+ String.format("% 5d", maxAllocatedMemory) + " MB" +
					" | Threads:"		+ String.format("% 3d", threads);
		   return s;			
		}
	}
	private void installGCMonitoring() {
		//get all the GarbageCollectorMXBeans - there's one for each heap generation
		//so probably two - the old generation and young generation
		List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
		//Install a notification handler for each bean
		for (GarbageCollectorMXBean gcbean : gcbeans) {
			newTracker((NotificationEmitter) gcbean);
		}
	}
	private void newTracker(NotificationEmitter gcbean) {
		NotificationEmitter emitter = gcbean;
		MemoryTrackerListener listener = new MemoryTrackerListener();
		emitter.addNotificationListener(listener, null, null);
	}
	private class MemoryTrackerListener implements NotificationListener {
		private static final String type = "com.sun.management.gc.notification"; // GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)
		@Override public void handleNotification(Notification notification, Object handback) {
			if (notification.getType().equals(type)) {
				isMemoryMonitored = true;
				getMemoryInfo();
				//System.out.println("GC: " + getMemoryInfo());
			}
		}
	}
}
