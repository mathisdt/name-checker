package org.zephyrsoft.name.checker;

import java.io.File;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class NameChecker {

	private static final Logger LOG = LoggerFactory.getLogger(NameChecker.class);

	/** expected file name pattern */
	@VisibleForTesting
	static final Pattern FILE_NAME_PATTERN = Pattern
		.compile("^(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})-(?<name>[^-]+)(?:-(?<speaker>[^-]+))?-(?<bitrate>\\d{1,3})kbps\\.mp3$");

	/**
	 * expected pattern for free-form text parts in the file name (name and
	 * speaker)
	 */
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

			LOG.info("file name {} matches the required scheme", fileName);
			LOG.debug("extracted data: date={}, name={}, speaker={}, bitrate={}", date, name, speaker, bitrate);

			// check free-form texts for illegal characters
			boolean nameOK = checkTextPart(name, "name");
			if (!nameOK) {
				return false;
			}
			if (speaker != null && !speaker.isEmpty()) {
				boolean speakerOK = checkTextPart(speaker, "speaker");
				if (!speakerOK) {
					return false;
				}
			}

			Multimap<String, String> allowedPrefixesToDefaultSpeakers = loadAllowedPrefixesWithDefaultSpeakers();

			// check that name is in allowed list
			// TODO list contains only prefixes, additional text allowed afterwards
			if (allowedPrefixesToDefaultSpeakers.keySet().contains(name)) {
				LOG.debug("name {} is in list of allowed names", name);
			} else {
				LOG.error("name {} is NOT in list of allowed names", name);
				return false;
			}

			// check for unnecessary speaker entry
			Collection<String> defaultSpeakers = allowedPrefixesToDefaultSpeakers.get(name);
			if (defaultSpeakers != null && !defaultSpeakers.isEmpty() && defaultSpeakers.contains(speaker)) {
				LOG.error("speaker {} is default for {}, don't specify it explicitly", speaker, name);
				return false;
			}

			return true;
		} else {
			LOG.error("file name {} does not match the required scheme", fileName);
			return false;
		}
	}

	private static boolean checkTextPart(String partValue, String partName) {
		Matcher nameMatcher = TEXT_PART_PATTERN.matcher(partValue);
		if (nameMatcher.matches()) {
			LOG.debug("{} {} has only legal characters", partName, partValue);
			return true;
		} else {
			LOG.error("{} {} contains illegal characters", partName, partValue);
			return false;
		}
	}

	private static Multimap<String, String> loadAllowedPrefixesWithDefaultSpeakers() {
		Multimap<String, String> map = HashMultimap.create();

		File csv = new File(System.getProperty("base.dir") + File.separator + "data" + File.separator + "prefixes.csv");

		if (!csv.exists()) {
			LOG.error("prefixes file {} was not found", csv.getAbsolutePath());
		}

		// TODO

		return map;
	}

}
