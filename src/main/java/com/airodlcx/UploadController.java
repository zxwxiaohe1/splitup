package com.airodlcx;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UploadController {
	@RequestMapping(value = "/test-sync/sync.html", produces = "text/plain;charset=UTF-8")
	public String sync(List<MultipartFile> file,HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		System.out.println(file.size());
		return "index.jsp";
	}
}
