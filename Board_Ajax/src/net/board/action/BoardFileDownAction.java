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
				
				//�� ���������� ��� ��Ʈ�� �����մϴ�.
				BufferedOutputStream  out2 = 
								new BufferedOutputStream(response.getOutputStream());
				
				//sFilePath�� ������ ���Ͽ� ���� �Է½�Ʈ���� �����մϴ�.
				BufferedInputStream  in = 
						new BufferedInputStream(new FileInputStream(sFilePath));
				
				
				
				
				)
		
		
		{
			int numRead;
			
			//read(b,0,b.length) :����Ʈ �迭b�� 0������ b.lengthũ�⸸ŭ �о�ɴϴ�.
			while((numRead = in.read(b,0,b.length)) != -1){//���� �����Ͱ� �����ϴ� ���
				out2.write(b,0, numRead);//����Ʈ �迭 b�� 0�� ���� numReadũ�⸸ŭ �������� ���
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		return null;
	}

}
