package helloworld;

public class MyBean {

    private int age;
    private String sex;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void toString1() {
        System.out.println("age:" + String.valueOf(getAge()) + ",sex:" + getSex());
    }

    public void toString2(String sex) {
        System.out.println("sex:" + sex);
    }

}
