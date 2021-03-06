package hello.hellospring.domain;

public class Member {
    private Long id; // 회원이 입력하는 id가 아니라 시스템이 정해주는 id
    private String name;

    // getter와 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}