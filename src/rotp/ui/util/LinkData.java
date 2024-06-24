package rotp.ui.util;

import static rotp.ui.util.AbstractParam.DO_FOLLOW;
import static rotp.ui.util.AbstractParam.DO_LOCK;

import java.util.List;

public class LinkData {
	final AbstractParam<?> src;
	final AbstractParam<?> aim;
	private final int action;
	final boolean srcUp;	// Source evolution trigger
	public final boolean aimUp;	// Link evolution effect
	public final String key;
	public LinkValue srcValue, aimValue;
	LinkData (AbstractParam<?> aim, int action, boolean srcUp, boolean aimUp, String key, AbstractParam<?> src) {
		this.aim	= aim;
		this.srcUp	= srcUp;
		this.aimUp	= aimUp;
		this.action	= action;
		this.src	= src;
		this.key	= key;
	}
	LinkData copy()	{ return new LinkData(aim, action, srcUp, aimUp, key, src); }
	boolean isInvalidLocalValue(LinkValue potentialSrcValue)	{
		srcValue = potentialSrcValue;
		src.convertValueToLink(this);
		if (aimUp)
			return aim.isInvalidLocalMax(this);
		else
			return aim.isInvalidLocalMin(this);
	}
	boolean isUpdateNeeded(LinkValue potentialSrcValue)	{
		srcValue = potentialSrcValue;
		src.convertValueToLink(this);
		if (aimUp)
			return aim.updateNeeded(aimValue, aimUp);
		else
			return aim.updateNeeded(aimValue, aimUp);
	}

	boolean isInvalidLinkedValue(LinkValue potentialSrcValue)	{
		// System.out.println();
//		System.out.println("LinkData.isInvalidLinkedValue: "+src.getCfgLabel()+ " to " +aim.getCfgLabel());
		srcValue = potentialSrcValue;
		src.convertValueToLink(this);
//		System.out.println("Source: "+ srcValue);
//		System.out.println("Target: "+ aimValue);

//		if (aim.getCfgLabel().equals("GALAXY_SIZE"))
//			System.out.println("aim.getCfgLabel().equals(\"GALAXY_SIZE\")");
			
		if (aimUp) {
			if (copy().aim.isInvalidLocalMax(this))
				return true;
		}
		else
			if (copy().aim.isInvalidLocalMin(this))
				return true;
		List<LinkData> linkList = aim.linkList();
		if (linkList == null)
			return false;
		for (LinkData entry : linkList) {
			if (entry.follow(aimUp)) {
				if (entry.copy().isInvalidLinkedValue(aimValue)) {
					return true;
				}
			}
		}
		return false;
	}
	void followValue(LinkValue finalValue)	{
		srcValue = finalValue;
		src.convertValueToLink(this);
		List<LinkData> linkList = aim.linkList();
		boolean updateNeeded = copy().aim.updateNeeded(aimValue, aimUp);
		if (!copy().aim.followValue(this)) {
			if (linkList == null) {
				if(updateNeeded)
					aim.set(this);
				return;
			}
		}
		if (linkList == null)
			return;
		// OK to follow
		if (updateNeeded)
			aim.set(this);
		for (LinkData entry : linkList) {
			if (entry.follow(aimUp))
				entry.copy().followValue(aimValue);
		}
	}
	boolean follow(boolean dir)		{ return dir == srcUp && action == DO_FOLLOW; }
	boolean locked(boolean dir)		{ return dir == srcUp && action == DO_LOCK; }
}
