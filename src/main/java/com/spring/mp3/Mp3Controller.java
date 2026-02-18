package com.spring.mp3;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import web.com.client.AudioTagProcessor;
import web.com.client.FileProcessor;

@Controller

public class Mp3Controller {

	@Value("${server.servlet.context-path}")
	private String mp3Context;
	@Value("${server.port}")
	private String myPort;

	@Value("${server.servlet.resource-path}")
	private String downloadPath;

	private static final Logger logger = LoggerFactory.getLogger(Mp3Controller.class);
	private FileProcessor fileProcessor = new FileProcessor();
	private AudioTagProcessor audioTagProcessor = new AudioTagProcessor();

	private Path fileStorageLocation = Paths.get("C:\\secure-uploads");

	@GetMapping("/*") // start application mapping
	public String getHome(HttpSession session, Model model) {

		this.setSessionAttributs(session);
		return "index"; // index is a short name of ../webapp/WEB-INF/jsp/index.jsp
	}

	private void setSessionAttributs(HttpSession session) {

		if (null == session.getAttribute("mp3Context")) {
			session.setAttribute("mp3Context", mp3Context); // read in JSP as ${sessionScope.mp3Context}
		}
		if (null == session.getAttribute("myPort")) {
			session.setAttribute("myPort", myPort);
		}
	}

	@GetMapping("/show_dashboard")
	public String getHomePage(HttpSession session, Model model) {

		this.setSessionAttributs(session);
		return "index"; // index is a short name of ../webapp/WEB-INF/jsp/index.jsp

	}

	@GetMapping("/show_format")
	public String getFormat(HttpSession session, Model model, @ModelAttribute("mp3Model") MP3CleanerModel mp3Model,
			@ModelAttribute("mp3Options") MP3CleanerOptions mp3Options) {

		this.setSessionAttributs(session);
		session.setAttribute("mp3Options", mp3Options);
		return "formatfiles"; // match to related formatfiles.jsp
	}

	@PostMapping("/do_format")
	public String formatFiles(HttpSession session, Model model, @ModelAttribute("mp3Model") MP3CleanerModel mp3Model,
			@RequestParam(required = false) String filetype) {

		this.setSessionAttributs(session);
		if (null == session.getAttribute("mp3Options")) {
			return "index";
		}
		MP3Processor mp3Processor = new MP3Processor();
		try {
			mp3Processor.formatMP3Directory(mp3Model);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "formatfiles";
	}

	@PostMapping("/do_ajax_format") // Ajax use web-form submit
	public String postAjaxFormat(HttpSession session, Model model,
			@ModelAttribute("mp3Model") MP3CleanerModel mp3Model) {

		this.setSessionAttributs(session);
		if (null == session.getAttribute("mp3Options")) {
			return "index";
		}
		MP3Processor mp3Processor = new MP3Processor();
		try {
			mp3Processor.formatMP3Directory(mp3Model);
			MP3CleanerOptions mp3Options = (MP3CleanerOptions) session.getAttribute("mp3Options");
			StringBuffer retval = this.getAjaxDirectoryList(mp3Options, mp3Model.getPath(), mp3Model.getFiletype());
			mp3Model.setResultBuffer(retval);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "fileslist";
	}

	@PostMapping("/do_json_format") // ajax use Json body request
	public String postAjaxFormat(HttpSession session, Model model, @ModelAttribute("mp3Model") MP3CleanerModel mp3Model,
			@RequestBody String postData) {
		this.setSessionAttributs(session);
		if (null == session.getAttribute("mp3Options")) {
			return "index";
		}
		MP3Processor mp3Processor = new MP3Processor();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			MP3CleanerModel mp3Model02 = objectMapper.readValue(postData, MP3CleanerModel.class);
			mp3Processor.formatMP3Directory(mp3Model02);
			MP3CleanerOptions mp3Options = (MP3CleanerOptions) session.getAttribute("mp3Options");
			StringBuffer retval = this.getAjaxDirectoryList(mp3Options, mp3Model02.getPath(), mp3Model02.getFiletype());
			mp3Model.setResultBuffer(retval);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "fileslist";
	}

	@PostMapping("/do_cleanup")
	public String clearUpMp3(HttpSession session, Model model, @ModelAttribute("mp3Model") MP3CleanerModel mp3Model,
			@RequestParam(required = false) String filetype) {

		this.setSessionAttributs(session);
		if (null == session.getAttribute("mp3Options")) {
			return "index";
		}
		AudioTagProcessor audioTag3Processor = new AudioTagProcessor();
		try {

			HashMap<String, String> updateFields = new HashMap<String, String>();
			updateFields.put("ALBUM", "");
			updateFields.put("TITLE", "");
			updateFields.put("ARTIST", "");
			updateFields.put("ALBUM_ARTIST", "");
			updateFields.put("YEAR", "0");
			updateFields.put("TRACK", "0");
			audioTag3Processor.cleanAttributesMP3Directory(mp3Model.getPath(), updateFields); // AudioFile audioFile,
																								// HashMap<String,
																								// String> updateFields

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "formatfiles";
	}

	@GetMapping("/show_rename")
	public String getRename(HttpSession session, Model model, @ModelAttribute("mp3Model") MP3CleanerModel mp3Model,
			@ModelAttribute("mp3Options") MP3CleanerOptions mp3Options) {
		this.setSessionAttributs(session);
		session.setAttribute("mp3Options", mp3Options);
		this.parseFileTypeDropDown(mp3Model);
		return "renamefiles"; // match to related renamefiles.jsp

	}

	@PostMapping("/do_rename")
	public String renameFiles(HttpSession session, Model model, @ModelAttribute("mp3Model") MP3CleanerModel mp3Model,
			@RequestParam(required = false) String filetype) {
		this.setSessionAttributs(session);
		this.parseFileTypeDropDown(mp3Model);
		MP3Processor mp3Processor = new MP3Processor();
		try {
			mp3Processor.renameMP3Directory(mp3Model);
			// MP3CleanerOptions mp3Options = (MP3CleanerOptions)
			// session.getAttribute("mp3Options");
			MP3CleanerOptions mp3Options = new MP3CleanerOptions();
			mp3Options.setSizeOption("on");
			mp3Options.setModifiedOption("on");
			StringBuffer retval = this.getAjaxDirectoryList(mp3Options, mp3Model.getPath(), mp3Model.getFiletype());
			mp3Model.setResultBuffer(retval);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		// return "renamefiles";
		return "fileslist";
	}

	@GetMapping("/api/data_get") // loads files list view as StringBuffer retval
	public String getAjaxData(HttpSession session, Model model, @ModelAttribute("mp3Model") MP3CleanerModel mp3Model,
			String dirPath, @RequestParam(required = false) String filetype,
			@RequestParam(required = false) String callerid) {

		StringBuffer retval = new StringBuffer("");
		this.setSessionAttributs(session);
		if (null == session.getAttribute("mp3Options") || session.isNew()) {
			retval.append("<script type='text/javasript'>window.parent.location='index'</script>");
			retval.append("<span class='req'>session expired. Please restart from the dashboard.</span>");
			mp3Model.setResultBuffer(retval);
			return "fileslist";
		}

		MP3CleanerOptions mp3Options = (MP3CleanerOptions) session.getAttribute("mp3Options");
		if ("rename".equalsIgnoreCase(callerid)) { // dont show mp3 attributes on rename files screen
			mp3Options = new MP3CleanerOptions();
			mp3Options.setSizeOption("on");
			mp3Options.setModifiedOption("on");
		}
		retval = this.getAjaxDirectoryList(mp3Options, dirPath, filetype);
		mp3Model.setResultBuffer(retval);
		downloadPath = dirPath;

		return "fileslist";

	}

	@GetMapping("/api/data_attr") // loads the attirbuts of the first MP3 file in current directory
	public String getAjaxAttributes(HttpSession session, Model model,
			@ModelAttribute("mp3Model") MP3CleanerModel mp3Model, String dirPath) {

		try {
			ArrayList<String> attirbutesList = audioTagProcessor.getAttribuesValue(dirPath);
			mp3Model.setAttributesList(attirbutesList);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return "attributeslist";

	}

	@PostMapping("/api/data") // Or @GetMapping, @PutMapping, @DeleteMapping depending on your HTTP method
	public String receiveAjaxData(HttpSession session, @RequestBody String dirPath, String filetype) {

		dirPath = dirPath.replace("\\\\", "\\");
		MP3CleanerOptions mp3Options = (MP3CleanerOptions) session.getAttribute("mp3Options");
		String retval = this.getAjaxDirectoryList(mp3Options, dirPath, filetype).toString();
		return retval;
	}

	@GetMapping("/download/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
		fileStorageLocation = Paths.get(downloadPath);
		Path filePath = this.fileStorageLocation.resolve(filename).normalize();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(filePath.toFile()));
		String contentType = Files.probeContentType(filePath);
		if (contentType == null) {
			contentType = "application/octet-stream"; // Fallback if type not determined
		}

		HttpHeaders headers = new HttpHeaders(); // Set Content-Disposition header attachment or inline
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.toFile().getName() + "\"");
		headers.setContentLength(filePath.toFile().length()); // Set Content-Length if known, for client progress
																// indicators
		return ResponseEntity.ok().headers(headers).contentLength(filePath.toFile().length())
				.contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	private StringBuffer getAjaxDirectoryList(MP3CleanerOptions mp3Options, String dirPath, String filetype) {

		ArrayList<HashMap<String, String>> list = fileProcessor.readFileDirtory(dirPath, "asc");
		StringBuffer buffer = new StringBuffer("");
		try {
			buffer = audioTagProcessor.listMP3Dirtory(list, this.getFieldsHeaders(mp3Options), mp3Context, filetype);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return buffer;
	}

	private StringBuffer parseFileTypeDropDown(MP3CleanerModel mp3Model) {
		StringBuffer retval = new StringBuffer("");
		String selected = "";
		if ("".equals(mp3Model.getFiletype())) {
			mp3Model.setFiletype(".mp3");
		}
		//mp3Model.setFiletype("all");
		int icount = mp3Model.getExtensions().size();
		for (int i = 0; i < icount; i++) {
			String option = mp3Model.getExtensions().get(i);
			if (mp3Model.getFiletype().equalsIgnoreCase(option)) {
				selected = " selected = 'selected' ";
			}
			retval.append("<option value='").append(option).append("' ");
			retval.append(selected).append(">").append(option).append("</option>\n");
			selected = "";
		}
		mp3Model.setDropDownTypes(retval.toString());
		return retval;
	}

	private ArrayList<String> getFieldsHeaders(MP3CleanerOptions mp3Options) {
		// set columns order here
		// to change column header set string like these samples "TRACK=TRACK_ID",
		// "ALBUM=Album Name" or "ALBUM_ARTIST=Narrator"
		ArrayList<String> fieldsList = new ArrayList<String>();
		fieldsList.add("#=#");
		fieldsList.add("NAME=NAME");

		if ("on".equalsIgnoreCase(mp3Options.getTrackOption())) {
			fieldsList.add("TRACK=TRACK");
		}
		if ("on".equalsIgnoreCase(mp3Options.getTitleOption())) {
			fieldsList.add("TITLE=TITLE");
		}
		if ("on".equalsIgnoreCase(mp3Options.getAlbumOption())) {
			fieldsList.add("ALBUM=ALBUM");
		}
		if ("on".equalsIgnoreCase(mp3Options.getArtistOption())) {
			fieldsList.add("ARTIST=ARTIST");
		}
		if ("on".equalsIgnoreCase(mp3Options.getReadbyOption())) {
			fieldsList.add("ALBUM_ARTIST=READ BY");
		}
		if ("on".equalsIgnoreCase(mp3Options.getYearOption())) {
			fieldsList.add("YEAR=YEAR");
		}

		if ("on".equalsIgnoreCase(mp3Options.getLengthOption())) {
			fieldsList.add("AUDIO_LENGTH=LENGTH");
		}
		if ("on".equalsIgnoreCase(mp3Options.getSizeOption())) {
			fieldsList.add("FILE_SIZE=SIZE");
		}

		if ("on".equalsIgnoreCase(mp3Options.getModifiedOption())) {
			fieldsList.add("LAST_MODIFIED=LAST MODIFIED");
		}

		return fieldsList;
	}

	// private void setAppProperties(Model model) {
	// model.addAttribute("mp3Context", mp3Context);
	// model.addAttribute("myPort", myPort);
	// }
	//
	// -------- Error handling
	public class MyErrorController implements ErrorController {

		@GetMapping("/error")
		public String handleError(HttpServletRequest request) {
			Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
			if (status != null) {
				Integer statusCode = Integer.valueOf(status.toString());
				if (statusCode == HttpStatus.NOT_FOUND.value()) {
					return "error/404";
				}
			}
			return "error/error";
		}

		public String getErrorPath() {
			return "/error";
		}
	}

	/***********************************/
	// @Value("${upload.path}")
	private String path;

	@PostMapping("/upload") // example of file Upload
							// https://medium.com/@AlexanderObregon/how-to-handle-file-uploads-and-downloads-with-spring-boot-84638463fd6f
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
		try {
			// Check if the file's content type is acceptable
			if (!file.getContentType().equals("image/jpeg")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Invalid file type. Only JPEG files are allowed.");
			}

			// Check the file's size
			if (file.getSize() > 1_000_000) { // 1 MB limit
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is too large. The size limit is 1 MB.");
			}

			// Save the file to the server
			file.transferTo(new java.io.File(path + file.getOriginalFilename()));
			return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Could not upload the file: " + e.getMessage());
		}
	}

}
