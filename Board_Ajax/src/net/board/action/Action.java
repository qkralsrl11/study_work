
package net.board.action;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public interface Action {

	
//Ʋ�� ����Ͻ� ��û���� �����ϰ� ������� ActionForward Ÿ������ ��ȯ�ϴ�
//�޼��尡 ���ǵǾ� �ֽ��ϴ�.
//Action :�������̽� ��
//ActionForward:��ȯ��
	

		public ActionForward execute(HttpServletRequest request,
									HttpServletResponse response)
		throws ServletException, IOException;
		

}


