package mod.br.profileManager;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

class TEST_UserProfiles {

	private ClientClasses clientObject = new TEST_ClientClasses();
	private UserProfiles userProfiles;
//	private AbstractParameter<?, ?, ClientClasses> param;
	
	private void init() {
		userProfiles = new UserProfiles("", "");
		userProfiles.initAndLoadProfiles(clientObject);		
	}
	
	@Test void initAndLoadProfiles_ClientClasses() {
		userProfiles = new UserProfiles("", "");
		assertEquals(false, userProfiles.isInitialized(), "Not yet initialized");
		userProfiles.initAndLoadProfiles(clientObject);
		assertEquals(true, userProfiles.isInitialized(), "Should be initialized");
		assertEquals(true
				, userProfiles.isParameterEnabled("MAXIMIZE EMPIRES SPACING")
				, "Should be initialized");
		assertEquals(true
				, userProfiles.isParameterEnabled("GUI RACE FILTER")
				, "Should be initialized");
		assertEquals(false
				, userProfiles.isParameterEnabled("NON EXISTANT")
				, "Should not be initialized");
	}

	@Test
	void processKey_U() {
		init();
		assertEquals(false
				, userProfiles.processKey(KeyEvent.VK_U, false, "", clientObject)
				, "This KeyEvent sould return false");
	}
}
