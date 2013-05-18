package test.testFiles.xmlTest;

public class Person {
	private String name;
	private String sex;
	private String tel;
	private Addresses addres;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Addresses getAddres() {
		return addres;
	}
	public void setAddres(Addresses addres) {
		this.addres = addres;
	}
	public String toString(){
		return "Person{" + 
		"addes=" + addres.toString() + 
		", name='" + name + '\'' + 
		", sex='" + sex + '\'' + 
		", tel='" + tel + '\'' + 
		"}\n";
	}

}














