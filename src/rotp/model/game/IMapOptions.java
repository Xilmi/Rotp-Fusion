package rotp.model.game;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;

public interface IMapOptions extends IBaseOptsTools {

	int HIDE_SYSTEM_NAME = 0;
	int SHOW_SYSTEM_NAME = 1;
	int SHOW_SYSTEM_DATA = 2;
	
	int SHOW_ALL_FLIGHTPATHS		= 0;
	int SHOW_IMPORTANT_FLIGHTPATHS	= 1;
	int SHOW_NO_FLIGHTPATHS			= 2;

	int SHOW_ALL_SHIPS		  = 0;
	int SHOW_NO_UNARMED_SHIPS = 1;
	int SHOW_ONLY_ARMED_SHIPS = 2;
	
	int SHOW_RANGES				 = 0;
	int SHOW_STARS_AND_RANGES	 = 1;
	int SHOW_STARS				 = 2;
	int SHOW_NO_STARS_AND_RANGES = 3;

	// ========================================================================
	// Galaxy Map options
	// Parameters not available in specific option panel.
	
	ParamBoolean showGridCircular	= new ParamBoolean(MOD_UI, "SHOW_GRID_CIRCULAR", false);
	default void toggleGridCircularDisplay()	{ showGridCircular.toggle();}
	default boolean showGridCircular()			{ return showGridCircular.get();}

	ParamInteger flightPathDisplay	= new ParamInteger(MOD_UI, "FLIGHT_PATH_DISPLAY",
			SHOW_IMPORTANT_FLIGHTPATHS, 0, 2, true);
	default void flightPathDisplay(int i)		{ flightPathDisplay.set(i);}
	default void toggleFlightPathDisplay(boolean reverse)	{ flightPathDisplay.toggle(reverse);}
	default boolean showFleetsOnly()			{ return flightPathDisplay.get() == SHOW_NO_FLIGHTPATHS; }
	default boolean showImportantFlightPaths() 	{ return flightPathDisplay.get() != SHOW_NO_FLIGHTPATHS; }
	default boolean showAllFlightPaths()		{ return flightPathDisplay.get() == SHOW_ALL_FLIGHTPATHS; }

	ParamInteger systemNameDisplay	= new ParamInteger(MOD_UI, "SYSTEM_NAME_DISPLAY",
			SHOW_SYSTEM_DATA, 0, 2, true);
	default void systemNameDisplay(int i)	{ systemNameDisplay.set(i);}
	default void toggleSystemNameDisplay(boolean reverse)	{ systemNameDisplay.toggle(reverse);}
	default boolean hideSystemNames()		{ return systemNameDisplay.get() == HIDE_SYSTEM_NAME; }
	default boolean showSystemNames()		{ return systemNameDisplay.get() == SHOW_SYSTEM_NAME; }
	default boolean showSystemData() 		{ return systemNameDisplay.get() == SHOW_SYSTEM_DATA; }

	ParamInteger shipDisplay		= new ParamInteger(MOD_UI, "SHIP_DISPLAY",
			SHOW_ALL_SHIPS, 0, 2, true);
	default void shipDisplay(int i)					{ shipDisplay.set(i);}
	default void toggleShipDisplay(boolean reverse)	{ shipDisplay.toggle(reverse);}
	default boolean showFriendlyTransports()		{ return shipDisplay.get() != SHOW_ONLY_ARMED_SHIPS; }
	default boolean showUnarmedShips()				{ return shipDisplay.get() == SHOW_ALL_SHIPS; }

	ParamInteger showShipRanges	= new ParamInteger(MOD_UI, "SHOW_SHIP_RANGES",
			SHOW_STARS_AND_RANGES, 0, 3, true);
	default void showShipRanges(int i)						{ showShipRanges.set(i);}
	default void toggleShipRangesDisplay(boolean reverse)	{ showShipRanges.toggle(reverse);}
	default boolean showShipRanges()	{
		return (showShipRanges.get() == SHOW_RANGES)
				|| (showShipRanges.get() == SHOW_STARS_AND_RANGES);
	}
	default boolean showStars()	{
		return (showShipRanges.get() == SHOW_STARS)
				|| (showShipRanges.get() == SHOW_STARS_AND_RANGES);
	}
	// ==================== GUI List Declarations ====================

	LinkedList<IParam> galaxyMapOptions = new LinkedList<>(
			Arrays.asList(
				showGridCircular, flightPathDisplay, systemNameDisplay,
				shipDisplay, showShipRanges
			));
}
