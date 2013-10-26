package org.zephyrsoft.name.checker;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NameChecker {
	
	private static final Logger LOG = LoggerFactory.getLogger(NameChecker.class);
	
	/** expected file name pattern */
	@VisibleForTesting
	static final Pattern FILE_NAME_PATTERN =
		Pattern
			.compile("^(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})-(?<name>[^-]+)(?:-(?<speaker>[^-]+))?-(?<bitrate>\\d{1,3})kbps\\.mp3$");
	
	/** expected pattern for free-form text parts in the file name (name and speaker) */
	@VisibleForTesting
	static final Pattern TEXT_PART_PATTERN = Pattern.compile("^[A-Za-z0-9_]+$");
	
	private File file = null;
	
	public NameChecker(String fileName) {
		file = new File(fileName);
	}
	
	public boolean check() {
		if (!file.exists()) {
			LOG.error("file {} does not exist", file.getAbsolutePath());
			return false;
		}
		
		if (!file.isFile()) {
			LOG.error("{} is not a file", file.getAbsolutePath());
			return false;
		}
		
		return checkFileName(file.getName());
	}
	
	@VisibleForTesting
	static boolean checkFileName(String fileName) {
		Matcher fileMatcher = FILE_NAME_PATTERN.matcher(fileName);
		if (fileMatcher.matches()) {
			String date = fileMatcher.group("day") + "." + fileMatcher.group("month") + "." + fileMatcher.group("year");
			String name = fileMatcher.group("name");
			String speaker = fileMatcher.group("speaker");
			String bitrate = fileMatcher.group("bitrate");
			
			// TODO check free-form texts for illegal characters
			// TODO check for unnecessary speaker entry (default specified)
			
			LOG.info("file name {} matches the required scheme");
			LOG.debug("extracted data: date={}, name={}, speaker={}, bitrate={}", date, name, speaker, bitrate);
			return true;
		} else {
			LOG.error("file name {} does not match the required scheme", fileName);
			return false;
		}
	}
	
}
