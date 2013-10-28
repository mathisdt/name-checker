package org.zephyrsoft.name.checker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

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

	@Test
	public void testCheckFileName() {
		assertFalse(NameChecker.checkFileName("completely-non-matching-file-name.mp3"));
		assertFalse(NameChecker.checkFileName("2013-09-01-Spaces are not allowed in name-48kbps.mp3"));
		assertFalse(NameChecker.checkFileName("2013-09-01-Seminar-Spaces are not allowed in speaker-48kbps.mp3"));

		// supply a relative value for "base.dir" - this is not intended but works for tests
		System.setProperty("base.dir", "src" + File.separator + "dist");

		assertFalse(NameChecker.checkFileName("2013-09-01-Bibelstunde-Joerg-48kbps.mp3"));
		assertTrue(NameChecker.checkFileName("2013-09-01-Bibelstunde-Stephan-48kbps.mp3"));
		assertTrue(NameChecker.checkFileName("2013-09-01-Jugendfreizeit-48kbps.mp3"));
	}

}
