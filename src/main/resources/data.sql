insert into department_group(name, deleted)
values ('IT', false);

insert into university_department(department_group_id, university, department, deleted)
values (1, '충남대학교', '컴퓨터공학과', false);

insert into member(name, email, password, profile_image, birth, gender, job, phone_number, member_role, certified, deleted)
values ('나회원', 'test@naver.com', '{bcrypt}$2a$10$SBjm1/QOtWNGz4jSS99mQee5cK8jGBlvvD0gLHE6bhygkF/pF6U.G', 'image.jpg', '2000-01-02', 'MALE', 'student', '010-1234-1234', 'MEMBER', false, false);
