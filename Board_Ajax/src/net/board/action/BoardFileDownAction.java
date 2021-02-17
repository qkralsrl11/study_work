package net.board.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoardFileDownAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		String fileName = request.getParameter("filename");
		
		String savePath = "boardupload";
		
		ServletContext context =request.getServletContext();
		String sDownloadPath =context.getRealPath(savePath);

		String sFilePath = sDownloadPath + "/" + fileName;
		System.out.println(sFilePath);

		byte b[] = new byte[4096];
		String sMimeType =context.getMimeType(sFilePath);
		System.out.println("sMimeType>>>" + sMimeType);
		
		if (sMimeType== null)
			sMimeType = "application /octet-stream";

		response.setContentType(sMimeType);
		
		String sEncoding = new String (fileName.getBytes("euc-kr"), "ISO-8859-1");
		System.out.println(sEncoding);
		
		response.setHeader("Content-Disposition", "attachment; filename=" + sEncoding );
		

		try(
				
				//웹 브라우저로의 출력 스트림 생성합니다.
				BufferedOutputStream  out2 = 
								new BufferedOutputStream(response.getOutputStream());
				
				//sFilePath로 지정한 파일에 대한 입력스트림을 생성합니다.
				BufferedInputStream  in = 
						new BufferedInputStream(new FileInputStream(sFilePath));
				
				
				
				
				)
		
		
		{
			int numRead;
			
			//read(b,0,b.length) :바이트 배열b의 0번부터 b.length크기만큼 읽어옵니다.
			while((numRead = in.read(b,0,b.length)) != -1){//읽을 데이터가 존재하는 경우
				out2.write(b,0, numRead);//바이트 배열 b의 0번 부터 numRead크기만큼 브라우저로 출력
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		return null;
	}

}
