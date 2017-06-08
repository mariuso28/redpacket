package org.rp.baseuser;

import org.rp.util.EmailValidator;
import org.rp.util.PhoneValidator;

public class GzProfile {
	private String contact;
	private String phone;
	private String password;
	private String nickname;
	private String email;
	private String icon;
	
	public GzProfile()
	{
	}
	
	public String validate(String vPassword,boolean exist,GzRole role)
	{
		String msg = "";
		if (exist == false)
			msg += checkEmail();
		msg += checkField("Contact",contact);
		if (role.equals(GzRole.ROLE_PLAY))
			msg += checkField("Nick Name",nickname);
		
		msg += checkPhone();
		if (!vPassword.isEmpty())									// password autogenerated
		{
			msg += checkField("Password",password);
			msg += checkField("Verify Password",vPassword);
			msg += checkPassword(vPassword);
		}
		if (!msg.isEmpty())
			return "Missing or invalid fields : " + msg.substring(0,msg.length()-1);
		
		return msg;
	}
	
	private String checkEmail()
	{
		String msg = checkField("Email",email);
		if (msg.isEmpty())
		{
			EmailValidator ev = new EmailValidator();
			if (!ev.validate(email))
				msg += "Email,";
		}
		return msg;
	}
	
	private String checkPhone()
	{
		String msg = checkField("Phone",phone);
		if (msg.isEmpty())
		{
			PhoneValidator pv = new PhoneValidator();
			if (!pv.validate(phone))
				msg += "Phone,";
		}
		return msg;
	}
	
	private String checkPassword(String vPassword)
	{
		if (!vPassword.equals(password))
		{
			return "Password/Verify Password mismatch - please fix";
		}
		
		if (password.length()<8)
			return "Password should be 8 chars or more - please fix";
		
		return "";
	}
	
	public static String checkField(String field,String value)
	{
		if (value==null || value.isEmpty())
			return field + ",";
		return "";
	}
	
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "GzProfile [contact=" + contact + ", phone=" + phone + ", password=" + password + ", nickname="
				+ nickname + ", email=" + email + ", icon=" + icon + "]";
	}
	
}
