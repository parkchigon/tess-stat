package com.vup.tess.global.constant;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ResultCode {

    RC_2S000000("2S000000", "성공"),
    RC_2C000000("2C000000", "서버내부오류"),
    RC_2C000001("2C000001", "파라미터요류"),
    
    RC_3C000000("3C000000", "유효하지 않은 세션정보"),
    RC_3C000001("3C000001", "세션정보만료"),
	
	RC_4A000001("4A000001", "공장그룹정보찾기 실패");
	

	private String code;
    private String msg;
 
    public void ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public String getCode() {
    	return this.code;
    }
    
    public String getMsg() {
    	return this.msg;
    }
}
