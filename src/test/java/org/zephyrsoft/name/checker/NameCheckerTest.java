package org.zephyrsoft.name.checker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class NameCheckerTest {
	
	@Test
	public void testFileNamePattern() {
		assertTrue(NameChecker.FILE_NAME_PATTERN.matcher("2013-09-01-Morgengottesdienst-48kbps.mp3").matches());
		assertTrue(NameChecker.FILE_NAME_PATTERN.matcher("2013-09-01-Nachmittagsgottesdienst-128kbps.mp3").matches());
		assertTrue(NameChecker.FILE_NAME_PATTERN.matcher("2013-09-01-Bibelstunde-Jemand_Anders-24kbps.mp3").matches());
	}
	
	@Test
	public void testTextPartPattern() {
		assertTrue(NameChecker.TEXT_PART_PATTERN.matcher("Ganzwaslanges_mitnochwasdran").matches());
		assertFalse(NameChecker.TEXT_PART_PATTERN.matcher("Ümläuté").matches());
		assertFalse(NameChecker.TEXT_PART_PATTERN.matcher("Was mit Leerzeichen").matches());
	}
	
	// TODO test NameChecker.checkFileName(String)
	
}
