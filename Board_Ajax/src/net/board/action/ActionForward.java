package net.board.action;

//ActionForward Ŭ������ Action�������̽����� ���� �����ϰ� �������
//������ �̵��ҋ� ����ϴ� Ŭ�����Դϴ�.
//�� Ŭ������ Redirect ���ΰ��� �������� �������� ��ġ�� ������ �ֽ��ϴ�.
//�̰��� FrontController���� ActionForwardŬ���� Ÿ������ ��ȯ����
//�������� �װ��� Ȯ���Ͽ� �ش��ϴ� ��û �������� �̵��մϴ�.

public class ActionForward {
	
	
	
	
	private boolean redirect=false;
	private String path=null;
	
	//property redirect�� is�޼ҵ�
	public boolean isRedirect() {
		//������Ƽ Ÿ����boolean�ΰ�� get��� is�տ� ���ϼ� �ֽ��ϴ�.
		return redirect;
				
	}
	public void setRedirect(boolean b) {
		this.redirect = b;
	}
	
	
	public String getPath() {
		return path;
	}

	public void setPath(String String) {
		path = String;
	}

	

	
	
	
	
	

}
