# Homepage_BE_ver3.0
### Kahluaproject Spring Boot version

<img src="https://github.com/user-attachments/assets/9b42ebb8-a85c-4146-8596-b50f797b02f6" height=70% width=70%>

🧷 https://kahluaband.com

# KAHLUA_BE
**KAHLUA BAND website** Backend using SpringBoot

## 프로젝트 소개
아~ 우리는 깔!깔!깔!깔루아!! 저희는 밴드부 깔루아입니다.

매년 정기 공연을 진행하면서 공연 참석 희망자 수요 조사만 진행하고 현장예매로 티켓을 판매하였습니다. 이에 따라 수요 조사가 정확하지 않다는 단점도 있었고 현장 예매에 많은 시간이 소모된다는 단점이 존재했습니다.

따라서, 저희는 깔루아 공연을 보러오시는 관객분들이 조금 더 편하게 예매를 하실 수 있도록 예매 사이트를 만들었습니다. 예매 뿐만 아니라 동아리를 홍보하고 신입생 지원기간에는 지원도 사이트에서 함께 진행할 수 있도록 하였습니다.

추가로 수동으로 진행했던 동아리방 예약과 공지사항 전송 기능까지 한 사이트에서 모두 진행할 수 있도록 동아리방 시간 예약 페이지와 게시판 페이지를 추가했습니다.

## Backend Member 

<div align="left">

이한재: [@leehanjay](https://github.com/leehanjay)

최승원: [@Seungwon326](https://github.com/Seungwon326)
</div>

## 개발 환경
* Java
* SpringBoot
* MySQL

## 주요 기능

**공연 예매하기**

* 새내기 예매/일반 예매 선택
* 새내기 인증 필요
* 예매 완료 시 예매 번호 메일 발송
* 3월 공연 시 소모임 참석 여부 확인
* 신청자 정보 엑셀로 연동
  

**동아리 소개**

* 동아리 사진, 활동 소개


**동아리 영상**

* Youtube API 연동


**동아리 지원하기**

* 작성한 내용 엑셀로 연동


**동방 시간 예약하기**

* WebSocket & Stomp 사용해서 실시간 예약 진행
* 팀/개인 단위로 예약

**공지사항/사담 게시판**

* 전체 공지 또는 사담 게시판 생성

**어드민 페이지**

* 전체 지원자 조회
* 전체 예매자 조회
* 지원자, 예매자 통계 확인
