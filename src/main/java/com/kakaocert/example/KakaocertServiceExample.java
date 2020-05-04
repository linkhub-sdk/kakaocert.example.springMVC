package com.kakaocert.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kakaocert.api.KakaocertException;
import com.kakaocert.api.KakaocertService;
import com.kakaocert.api.esign.RequestESign;
import com.kakaocert.api.esign.ResultESign;
import com.kakaocert.api.verifyauth.RequestVerifyAuth;
import com.kakaocert.api.verifyauth.ResultVerifyAuth;

@Controller
public class KakaocertServiceExample {

	@Autowired
    private KakaocertService kakaocertService;
	
	// 이용기관코드
	// 파트너가 등록한 이용기관의 코드, (파트너 사이트에서 확인가능)
    @Value("#{EXAMPLE_CONFIG.ClientCode}")
    private String ClientCode;
    
    /*
	 * 카카오톡 사용자에게 전자서명을 요청합니다.
	 */
	@RequestMapping(value = "requestESign", method = RequestMethod.GET)
	public String requestESign(Model m) {
		
		// 전자서명 요청 정보 Object
		RequestESign request = new RequestESign();
		
		
		// 고객센터 전화번호	, 카카오톡 인증메시지 중 "고객센터" 항목에 표시
		request.setCallCenterNum("1600-9999");
		
		// 인증요청 만료시간(초), 최대값 : 99,999,999,	인증요청 만료시간(초) 내에 미인증시, 만료 상태로 처리됨
		request.setExpires_in(60);
		
		// PayLoad, 이용기관이 API 요청마다 생성한 payload(메모) 값
		request.setPayLoad("memo Info");
		
		// 수신자 생년월일, 형식 : YYYYMMDD
		request.setReceiverBirthDay("19900108");
		
		// 수신자 휴대폰번호	
		request.setReceiverHP("01043245117");
		
		// 수신자 성명	
		request.setReceiverName("정요한");
		
		// 인증요청 메시지 부가내용, 카카오톡 인증메시지 중 상단에 표시
		request.setTMSMessage("부가메시지 내용");
		
		// 별칭코드, 이용기관이 생성한 별칭코드 (파트너 사이트에서 확인가능)
		// 카카오톡 인증메시지 중 "요청기관" 항목에 표시
		// 별칭코드 미 기재시 이용기관의 이용기관명이 "요청기관" 항목에 표시
		request.setSubClientID("");
		
		// 인증요청 메시지 제목, 카카오톡 인증메시지 중 "요청구분" 항목에 표시
		request.setTMSTitle("TMS Title");
		
		// 전자서명 원문
		request.setToken("token value");
		
		/*
		 * 은행계좌 실명확인 생략여부	
		 * true : 은행계좌 실명확인 절차를 생략
     	 * false : 은행계좌 실명확인 절차를 진행
     	 * 
		 * - 인증메시지를 수신한 사용자가 카카오인증 비회원일 경우,
		 *   카카오인증 회원등록 절차를 거쳐 은행계좌 실명확인 절차이후 전자서명 가능
		 */
		request.setAllowSimpleRegistYN(false);
		
		/*
		 * 수신자 실명확인 여부 	
		 * true : 카카오페이가 본인인증을 통해 확보한 사용자 실명과 ReceiverName 값을 비교
		 * false : 카카오페이가 본인인증을 통해 확보한 사용자 실명과 RecevierName 값을 비교하지 않음.	
		 */
		request.setVerifyNameYN(true);
		
		
		try {
			
			String receiptID = kakaocertService.requestESign(ClientCode, request);
			
			m.addAttribute("Result", receiptID);
			
		} catch(KakaocertException ke) {
			m.addAttribute("Exception", ke);
			return "exception";
		}
		
		return "result";
		   
	}
	
	/*
     * 간편 전자서명 요청시 반환된 접수아이디를 통해 전자서명 결과를 확인합니다.
     */
	@RequestMapping(value = "getESignResult", method = RequestMethod.GET)
    public String getESignResult(Model m) {
        
		// 전자서명 요청시 반환된 접수아이디 
		String receiptID = "020050412211500001";

        try {
            ResultESign result = kakaocertService.getESignResult(ClientCode, receiptID);
            m.addAttribute("result", result);
        } catch(KakaocertException ke) {
			m.addAttribute("Exception", ke);
			return "exception";
        }
        return "getESignResult";
    }
	
	/*
	 * 카카오톡 사용자에게 본인인증 전자서명을 요청합니다.
	 */
	@RequestMapping(value = "requestVerifyAuth", method = RequestMethod.GET)
	public String requestVerifyAuth(Model m) {
		
		// 본인인증 요청 정보 Object
		RequestVerifyAuth request = new RequestVerifyAuth();
		
		// 고객센터 전화번호	, 카카오톡 인증메시지 중 "고객센터" 항목에 표시
		request.setCallCenterNum("1600-9999");
		
		// 인증요청 만료시간(초), 최대값 : 99,999,999,	인증요청 만료시간(초) 내에 미인증시, 만료 상태로 처리됨
		request.setExpires_in(60);
		
		// PayLoad, 이용기관이 API 요청마다 생성한 payload(메모) 값
		request.setPayLoad("memo Info");
		
		// 수신자 생년월일, 형식 : YYYYMMDD
		request.setReceiverBirthDay("19900108");
		
		// 수신자 휴대폰번호	
		request.setReceiverHP("01043245117");
		
		// 수신자 성명	
		request.setReceiverName("정요한");
		
		// 인증요청 메시지 부가내용, 카카오톡 인증메시지 중 상단에 표시
		request.setTMSMessage("부가메시지 내용");
		
		// 별칭코드, 이용기관이 생성한 별칭코드 (파트너 사이트에서 확인가능)
		// 카카오톡 인증메시지 중 "요청기관" 항목에 표시
		// 별칭코드 미 기재시 이용기관의 이용기관명이 "요청기관" 항목에 표시
		request.setSubClientID("");
		
		// 인증요청 메시지 제목, 카카오톡 인증메시지 중 "요청구분" 항목에 표시
		request.setTMSTitle("TMS Title");
		
		// 원문, 보안을 위해 1회용으로 생성
		// 인증완료시, getVerifyAuthResult API의 returnToken 항목값으로 반환
		request.setToken("20200504-01");
		
		/*
		 * 은행계좌 실명확인 생략여부	
		 * true : 은행계좌 실명확인 절차를 생략
     	 * false : 은행계좌 실명확인 절차를 진행
     	 * 
		 * - 인증메시지를 수신한 사용자가 카카오인증 비회원일 경우,
		 *   카카오인증 회원등록 절차를 거쳐 은행계좌 실명확인 절차이후 전자서명 가능
		 */
		request.setAllowSimpleRegistYN(false);
		
		/*
		 * 수신자 실명확인 여부 	
		 * true : 카카오페이가 본인인증을 통해 확보한 사용자 실명과 ReceiverName 값을 비교
		 * false : 카카오페이가 본인인증을 통해 확보한 사용자 실명과 RecevierName 값을 비교하지 않음.	
		 */
		request.setVerifyNameYN(true);
		
		
		try {
			
			String receiptID = kakaocertService.requestVerifyAuth(ClientCode, request);
			
			m.addAttribute("Result", receiptID);
			
		} catch(KakaocertException ke) {
			m.addAttribute("Exception", ke);
			return "exception";
		}
		
		return "result";
		   
	}
	
	/*
     * 본인인증 요청시 반환된 접수아이디를 통해 본인인증 결과를 확인합니다.
     */
	@RequestMapping(value = "getVerifyAuthResult", method = RequestMethod.GET)
    public String getVerifyAuthResult(Model m) {
        
		// 본인인증 요청시 반환된 접수아이디 
		String receiptID = "020050413311700001";

        try {
            ResultVerifyAuth result = kakaocertService.getVerifyAuthResult(ClientCode, receiptID);
            m.addAttribute("result", result);
        } catch(KakaocertException ke) {
			m.addAttribute("Exception", ke);
			return "exception";
        }
        return "getVerifyAuthResult";
    }
}
