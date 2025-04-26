-- 초기 데이터 sql 파일
insert into notification_template (noti_template_nm, noti_title, noti_content, noti_type, create_user) values('회원가입','회원가입을 축하드립니다.','{userNum} 님 코문철에 가입을 축하드립니다.','NOTI',null);
insert into notification_template (noti_template_nm, noti_title, noti_content, noti_type, create_user) values('회원탈퇴','회원탈퇴 되었습니다.','{userNum} 님 코문철에 탈퇴되었습니다.','NOTI',null);
insert into notification_template (noti_template_nm, noti_title, noti_content, noti_type, create_user) values('그룹초대','그룹에 초대 되었습니다.','{groupNm} 그룹에 초대 되었습니다.','JOIN',null);
insert into notification_template (noti_template_nm, noti_title, noti_content, noti_type, create_user) values('그룹나가기','그룹에서 나갔습니다.','{groupNm} 그룹에서 나갔습니다.','NOTI',null);
insert into notification_template (noti_template_nm, noti_title, noti_content, noti_type, create_user) values('그룹내보내기','그룹에서 퇴출 되었습니다.','{userNum} 님께서 {groupNM} 그룹에서 퇴출하였습니다.','NOTI',null);
insert into notification_template (noti_template_nm, noti_title, noti_content, noti_type, create_user) values('댓글등록','댓글이 등록되었습니다.','{review-Title} 게시글에 {userNum} 님 께서 댓글을 등록 했습니다.','LINK',null);
insert into notification_template (noti_template_nm, noti_title, noti_content, noti_type, create_user) values('리뷰요청','리뷰요청이 도착했어요.','{userNm} 님이 리뷰를 요청했어요.<br/>리뷰 제목: {title}','LINK',null);