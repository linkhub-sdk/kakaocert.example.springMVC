package com.kakaocert.example;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kakaocert.api.KakaocertException;
import com.kakaocert.api.KakaocertService;
import com.kakaocert.api.ResponseCMS;
import com.kakaocert.api.ResponseESign;
import com.kakaocert.api.VerifyResult;
import com.kakaocert.api.cms.RequestCMS;
import com.kakaocert.api.cms.ResultCMS;
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

    @RequestMapping(value = "CheckKakaoServiceAttribute")
    public String CheckKakaoServiceAttribute(Model m) {
        kakaoCert2ModelAttribute(m);
        return "CheckKakaoServiceAttribute";
    }

    private void kakaoCert2ModelAttribute(Model m) {
        Field[] fields = kakaocertService.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                m.addAttribute(field.getName(), field.get(kakaocertService));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 카카오톡 사용자에게 전자서명을 요청합니다.
     */
    @RequestMapping(value = "requestESign", method = RequestMethod.GET)
    public String requestESign(Model m) {

        // 전자서명 요청 정보 Object
        RequestESign request = new RequestESign();

        // AppToApp 인증요청 여부
        // true - AppToApp 인증방식, false - Talk Message 인증방식
        boolean isAppUseYN = false;

        // 고객센터 전화번호 , 카카오톡 인증메시지 중 "고객센터" 항목에 표시
        request.setCallCenterNum("1600-1234");

        // 고객센터명 , 카카오톡 인증메시지 중 "고객센터명" 항목에 표시
        request.setCallCenterName("고객센터명");

        // 인증요청 만료시간(초), 최대값 : 1000, 인증요청 만료시간(초) 내에 미인증시, 만료 상태로 처리됨
        request.setExpires_in(60);

        // 수신자 생년월일, 형식 : YYYYMMDD
        request.setReceiverBirthDay("19700101");

        // 수신자 휴대폰번호
        request.setReceiverHP("01012341234");

        // 수신자 성명
        request.setReceiverName("홍길동");

        // 인증요청 메시지 제목, 카카오톡 인증메시지 중 "요청구분" 항목에 표시
        request.setTMSTitle("TMS Title");

        // 인증요청 메시지 부가내용, 카카오톡 인증메시지 중 상단에 표시
        // AppToApp 인증요청 방식 이용시 적용되지 않음
        request.setTMSMessage("부가메시지 내용");

        // 별칭코드, 이용기관이 생성한 별칭코드 (파트너 사이트에서 확인가능)
        // 카카오톡 인증메시지 중 "요청기관" 항목에 표시
        // 별칭코드 미 기재시 이용기관의 이용기관명이 "요청기관" 항목에 표시
        // AppToApp 인증요청 방식 이용시 적용되지 않음
        request.setSubClientID("");

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

        // PayLoad, 이용기관이 API 요청마다 생성한 payload(메모) 값
        request.setPayLoad("memo Info");

        try {

            ResponseESign receiptID = kakaocertService.requestESign(ClientCode, request, isAppUseYN);

            m.addAttribute("receiptId", receiptID.getReceiptId());
            m.addAttribute("tx_id", receiptID.getTx_id());

        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }

        return "resultESign";
    }

    /*
     * 전자서명 요청시 반환된 접수아이디를 통해 서명 상태를 확인합니다.
     */
    @RequestMapping(value = "getESignState", method = RequestMethod.GET)
    public String getESignResult(Model m) {

        // 전자서명 요청시 반환된 접수아이디
        String receiptID = "022051115151400001";

        try {
            ResultESign result = kakaocertService.getESignState(ClientCode, receiptID);
            m.addAttribute("result", result);
        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }
        return "getESignState";
    }

    /*
     * [Talk Message] 전자서명 요청시 반환된 접수아이디를 통해 서명을 검증합니다.
     * - 서명검증시 전자서명 데이터 전문(signedData)이 반환됩니다.
     * - 카카오페이 API 서비스 운영정책에 따라 검증 API는 1회만 호출할 수 있습니다. 재시도시 오류처리됩니다.
     */
    @RequestMapping(value = "verifyESign", method = RequestMethod.GET)
    public String verfiyESign(Model m) {

        // 전자서명 요청시 반환된 접수아이디
        String receiptID = "022051115151400001";

        try {
            VerifyResult result = kakaocertService.verifyESign(ClientCode, receiptID);
            m.addAttribute("result", result);
        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }
        return "responseVerify";
    }

    /*
     * [App to App] 전자서명 요청시 반환된 접수아이디를 통해 서명을 검증합니다.
     * - 서명검증시 전자서명 데이터 전문(signedData)이 반환됩니다.
     * - 카카오페이 API 서비스 운영정책에 따라 검증 API는 1회만 호출할 수 있습니다. 재시도시 오류처리됩니다.
     */
    @RequestMapping(value = "verifyESignApp", method = RequestMethod.GET)
    public String verfiyESignApp(Model m) {

        // 전자서명 요청시 반환된 접수아이디
        String receiptID = "022050915342900001";

        // AppToApp 앱스킴 성공처리시 반환되는 서명값(iOS-sig, Android-signature)
        String signature = "abc";

        try {
            VerifyResult result = kakaocertService.verifyESign(ClientCode, receiptID, signature);
            m.addAttribute("result", result);
        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }
        return "responseVerify";
    }

    /*
     * 카카오톡 사용자에게 본인인증 전자서명을 요청합니다.
     */
    @RequestMapping(value = "requestVerifyAuth", method = RequestMethod.GET)
    public String requestVerifyAuth(Model m) {

        // 본인인증 요청 정보 Object
        RequestVerifyAuth request = new RequestVerifyAuth();

        // 고객센터 전화번호 , 카카오톡 인증메시지 중 "고객센터" 항목에 표시
        request.setCallCenterNum("1600-1234");

        // 고객센터명 , 카카오톡 인증메시지 중 "고객센터명" 항목에 표시
        request.setCallCenterName("고객센터명");

        // 인증요청 만료시간(초), 최대값 : 1000, 인증요청 만료시간(초) 내에 미인증시, 만료 상태로 처리됨
        request.setExpires_in(60);

        // 수신자 생년월일, 형식 : YYYYMMDD
        request.setReceiverBirthDay("19700101");

        // 수신자 휴대폰번호
        request.setReceiverHP("01012341234");

        // 수신자 성명
        request.setReceiverName("홍길동");

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
        request.setToken("20220504-01");

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

        // PayLoad, 이용기관이 API 요청마다 생성한 payload(메모) 값
        request.setPayLoad("memo Info");

        try {

            String receiptID = kakaocertService.requestVerifyAuth(ClientCode, request);

            m.addAttribute("Result", receiptID);

        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }

        return "result";

    }

    /*
     * 본인인증 요청시 반환된 접수아이디를 통해 서명 상태를 확인합니다.
     */
    @RequestMapping(value = "getVerifyAuthState", method = RequestMethod.GET)
    public String getVerifyAuthState(Model m) {

        // 본인인증 요청시 반환된 접수아이디
        String receiptID = "022050610142700001";

        try {
            ResultVerifyAuth result = kakaocertService.getVerifyAuthState(ClientCode, receiptID);
            m.addAttribute("result", result);
        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }
        return "getVerifyAuthState";
    }

    /*
     * 본인인증 요청시 반환된 접수아이디를 통해 본인인증 서명을 검증합니다.
     * - 서명검증시 전자서명 데이터 전문(signedData)이 반환됩니다.
     * - 본인인증 요청시 작성한 Token과 서명 검증시 반환되는 signedData의 동일여부를 확인하여 본인인증 검증을 완료합니다.
     * - 카카오페이 API 서비스 운영정책에 따라 검증 API는 1회만 호출할 수 있습니다. 재시도시 오류처리됩니다.
     */
    @RequestMapping(value = "verifyAuth", method = RequestMethod.GET)
    public String verifyAuth(Model m) {

        // 본인인증 요청시 반환된 접수아이디
        String receiptID = "022050915411700001";

        try {
            VerifyResult result = kakaocertService.verifyAuth(ClientCode, receiptID);
            m.addAttribute("result", result);
        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }
        return "responseVerify";
    }

    /*
     * 카카오톡 사용자에게 자동이체 출금동의 전자서명을 요청합니다.
     */
    @RequestMapping(value = "requestCMS", method = RequestMethod.GET)
    public String requestCMS(Model m) {

        // 자동이체 출금동의 요청 정보 Object
        RequestCMS request = new RequestCMS();

        // AppToApp 인증요청 여부
        // true - AppToApp 인증방식, false - Talk Message 인증방식
        boolean isAppUseYN = false;

        // 고객센터 전화번호 , 카카오톡 인증메시지 중 "고객센터" 항목에 표시
        request.setCallCenterNum("1600-1234");

        // 고객센터명 , 카카오톡 인증메시지 중 "고객센터명" 항목에 표시
        request.setCallCenterName("고객센터명");

        // 인증요청 만료시간(초), 최대값 : 1000, 인증요청 만료시간(초) 내에 미인증시, 만료 상태로 처리됨
        request.setExpires_in(60);

        // 수신자 생년월일, 형식 : YYYYMMDD
        request.setReceiverBirthDay("19700101");

        // 수신자 휴대폰번호
        request.setReceiverHP("01012341234");

        // 수신자 성명
        request.setReceiverName("홍길동");

        // 예금주명
        request.setBankAccountName("예금주명");

        // 계좌번호, 이용기관은 사용자가 식별가능한 범위내에서 계좌번호의 일부를 마스킹 처리할 수 있음 예시) 371-02-6***85
        request.setBankAccountNum("9-4**4-1234-58");

        // 참가기관 코드
        request.setBankCode("004");

        // 납부자 식별번호, 이용기관에서 부여한 고객식별번호
        request.setClientUserID("20220504-001");

        // 별칭코드, 이용기관이 생성한 별칭코드 (파트너 사이트에서 확인가능)
        // 카카오톡 인증메시지 중 "요청기관" 항목에 표시
        // 별칭코드 미 기재시 이용기관의 이용기관명이 "요청기관" 항목에 표시
        request.setSubClientID("");

        // 인증요청 메시지 제목, 카카오톡 인증메시지 중 "요청구분" 항목에 표시
        request.setTMSTitle("TMS Title");

        // 인증요청 메시지 부가내용, 카카오톡 인증메시지 중 상단에 표시
        request.setTMSMessage("부가메시지 내용");

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

        // PayLoad, 이용기관이 API 요청마다 생성한 payload(메모) 값
        request.setPayLoad("memo Info");

        try {

            ResponseCMS requestCMS = kakaocertService.requestCMS(ClientCode, request, isAppUseYN);

            m.addAttribute("receiptId", requestCMS.getReceiptId());
            m.addAttribute("tx_id", requestCMS.getTx_id());

        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }

        return "resultCMS";

    }

    /*
     * 자동이체 출금동의 요청시 반환된 접수아이디를 통해 서명 상태를 확인합니다.
     */
    @RequestMapping(value = "getCMSState", method = RequestMethod.GET)
    public String getCMSState(Model m) {

        // 출금동의 요청시 반환된 접수아이디
        String receiptID = "022050610145900001";

        try {
            ResultCMS result = kakaocertService.getCMSState(ClientCode, receiptID);
            m.addAttribute("result", result);
        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }
        return "getCMSState";
    }

    /*
     * [Talk Message] 자동이체 출금동의 요청시 반환된 접수아이디를 통해 서명을 검증합니다.
     * - 서명검증시 전자서명 데이터 전문(signedData)이 반환됩니다.
     * - 카카오페이 API 서비스 운영정책에 따라 검증 API는 1회만 호출할 수 있습니다. 재시도시 오류처리됩니다.
     */
    @RequestMapping(value = "verifyCMS", method = RequestMethod.GET)
    public String verifyCMS(Model m) {

        // 출금동의 요청시 반환된 접수아이디
        String receiptID = "022050915424200001";

        try {
            VerifyResult result = kakaocertService.verifyCMS(ClientCode, receiptID);
            m.addAttribute("result", result);
        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }
        return "responseVerify";
    }

    /*
     * [App to App] 자동이체 출금동의 요청시 반환된 접수아이디를 통해 서명을 검증합니다.
     * - 서명검증시 전자서명 데이터 전문(signedData)이 반환됩니다.
     * - 카카오페이 API 서비스 운영정책에 따라 검증 API는 1회만 호출할 수 있습니다. 재시도시 오류처리됩니다.
     */
    @RequestMapping(value = "verifyCMSApp", method = RequestMethod.GET)
    public String verifyCMSApp(Model m) {

        // 출금동의 요청시 반환된 접수아이디
        String receiptID = "022050915424200001";

        // AppToApp 앱스킴 성공처리시 반환되는 서명값(iOS-sig, Android-signature)
        String signature = "abc";

        try {
            VerifyResult result = kakaocertService.verifyCMS(ClientCode, receiptID, signature);
            m.addAttribute("result", result);
        } catch (KakaocertException ke) {
            m.addAttribute("Exception", ke);
            return "exception";
        }
        return "responseVerify";
    }
}
