package net.board.action;

//ActionForward 클래스는 Action인터페이스에서 명열을 수행하고 결과값을
//가지고 이동할떄 사용하는 클래스입니다.
//이 클래스는 Redirect 여부값과 포워딩할 페이지의 위치를 가지고 있습니다.
//이값을 FrontController에서 ActionForward클래스 타입으로 반환값을
//가져오면 그값을 확인하여 해당하는 요청 페이지로 이동합니다.

public class ActionForward {
	
	
	
	
	private boolean redirect=false;
	private String path=null;
	
	//property redirect의 is메소드
	public boolean isRedirect() {
		//프로퍼티 타입이boolean인경우 get대신 is앞에 붙일수 있습니다.
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
