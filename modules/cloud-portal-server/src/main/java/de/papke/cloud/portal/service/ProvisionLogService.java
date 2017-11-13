package de.papke.cloud.portal.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.papke.cloud.portal.dao.ProvisionLogDao;
import de.papke.cloud.portal.pojo.ProvisionLog;
import de.papke.cloud.portal.pojo.User;

@Service
public class ProvisionLogService {

	private static final Logger LOG = LoggerFactory.getLogger(ProvisionLogService.class);

	private static final String TMP_FILE_PREFIX = "provision-log";
	private static final String TMP_FILE_SUFFIX = ".zip";

	@Autowired
	private UserService userService;

	@Autowired
	private ProvisionLogDao provisionLogDao;

	public List<ProvisionLog> getList(String provider) {

		List<ProvisionLog> provisionLogList = new ArrayList<>();
		
		User user = userService.getUser();
		if (user != null) {
			String username = user.getUsername();
			provisionLogList = provisionLogDao.findByUsernameAndProvider(username, provider);
		}

		return provisionLogList;
	}
	
	public ProvisionLog get(String id) {
		return provisionLogDao.findById(id);
	}

	public ProvisionLog create(String command, String provider, Boolean success, Map<String, Object> variableMapWithCredentials, File tmpFolder) {

		ProvisionLog provisionLog = null;
		File zipFile = null;

		try {

			// get username;
			User user = userService.getUser();
			String username = user.getUsername();

			// zip temp folder
			zipFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
			ZipService.zip(tmpFolder, zipFile);
			byte[] data = IOUtils.toByteArray(new FileInputStream(zipFile));

			// remove credentials from variable map
			Map<String, Object> variableMap = new HashMap<>();
			for (String key : variableMapWithCredentials.keySet()) {
				if (!key.startsWith("credentials-")) {
					Object value = variableMapWithCredentials.get(key);
					variableMap.put(key, value);
				}
			}
			
			// create provision log
			provisionLog = provisionLogDao.save(new ProvisionLog(new Date(), username, command, provider, success, variableMap, data));
		}
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		finally {
			if (zipFile != null) {
				zipFile.delete();
			}
		}

		return provisionLog;
	}
	
	public void delete(String id) {
		provisionLogDao.delete(id);
	}
}
