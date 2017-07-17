package com.uf.store.webservice.action.storemanager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.uf.store.service.ProductManageService;
import com.uf.store.util.SpringBeanFactory;
import com.uf.store.webservice.action.CommonAction;
import com.uf.store.webservice.bean.WebServiceResponse;
import com.uf.store.webservice.bean.WebServiceResponse.ResultCode;

@Singleton
@Path("/manager")
public class ProductManageAction extends CommonAction {
	private ProductManageService productManageService = SpringBeanFactory.getBean(ProductManageService.class);

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@GET
	@Path("/login")
	public String login(@QueryParam("userName") String userName, @QueryParam("password") String pwd) {
		System.out.println("hello");
		return null;
	}

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@GET
	@Path("/logout")
	public String logout(@HeaderParam("sessionId") String sessionId) {
		return null;
	}

	@POST
	@Path("/uploadProductImg")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadProductImg(FormDataMultiPart uploadBody, @Context HttpServletRequest request) {
		UploadImgResponse result = new UploadImgResponse();
		try {
			FormDataBodyPart 	fileBody=uploadBody.getField("imgFile");
			String fileName=fileBody.getContentDisposition().getFileName();
			File file=fileBody.getValueAs(File.class);
			String sessionId = request.getSession().getId();
			File imgPath = new File(request.getServletContext().getRealPath("/") + "tmp/" + sessionId);
			if (!imgPath.exists()) {
				imgPath.mkdirs();
			}

			String extend =Files.getFileExtension(fileName);
			File productImg = new File(imgPath, UUID.randomUUID().toString() + extend);
			productImg.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(productImg);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(ResultCode.FAIL);
		}
		Gson gson = new Gson();
		return gson.toJson(result);
	}
	private class UploadImgResponse extends WebServiceResponse{
		private String imgTmpUrl;

		public String getImgTmpUrl() {
			return imgTmpUrl;
		}

		public void setImgTmpUrl(String imgTmpUrl) {
			this.imgTmpUrl = imgTmpUrl;
		}

	}


}
