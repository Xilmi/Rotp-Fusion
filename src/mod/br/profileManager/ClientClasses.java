package mod.br.profileManager;

import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;

/**
 * @author BrokenRegistry
 * Classes that have to be passed thru Profile manager
 * Could be replaced by using {@code Object} and casting the class
 */
public class ClientClasses {
	private IGameOptions newOption;
	private IGameOptions option;
	private GameSession  session;

	// ==================================================
	// Constructors and initializers
	//
	/**
	 * Just a new empty class!
	 */
	public ClientClasses() {
	}
	/**
	 * @param guiObject {@code IGameOptions} to set
	 */
	public ClientClasses(IGameOptions guiObject) {
		newOption = guiObject;
		option = guiObject;
	}
	/**
	 * @param options {@code IGameOptions} to set
	 * @param newOptions {@code IGameOptions} to set
	 */
	public ClientClasses(IGameOptions options, IGameOptions newOptions) {
		newOption = newOptions;
//		option = options;
		option = newOptions; // TODO BR: Validate
	}
	/**
	 * @param gameObject {@code GameSession} to set
	 */
	public ClientClasses(GameSession gameObject) {
		session = gameObject;
		newOption = gameObject.options();
		option = newOption;
	}
	// ==================================================
	// Getters
	//
	/**
	 * @return the guiObject or the Session().options()
	 */
	public IGameOptions newOptions() {
		return newOption;
	}
	/**
	 * @return the second guiObject
	 */
	public IGameOptions options() {
		return option;
	}
	/**
	 * @return the gameObject
	 */
	public GameSession session() {
		return session;
	}
}
