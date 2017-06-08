package org.rp.web.admin;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rp.admin.GzAdmin;
import org.rp.admin.GzAdminProperties;
import org.rp.baseuser.GzProfile;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.util.StackDump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes({"currUser","currStackDump"})

@RequestMapping("/admin")
public class GzAdminController {

	private static Logger log = Logger.getLogger(GzAdminController.class);	
	private GzServices gzServices;
	
	
	// /gz/admin/logon?user&email=
	@RequestMapping(value = "/logon", params="user", method = RequestMethod.GET)
	public Object signin(String email,ModelMap model,HttpServletRequest request,RedirectAttributes stack) {
		
		log.info("Received request to logon");
			
		GzAdmin currUser = null;
	
		try {
			currUser = gzServices.getGzHome().getAdminByEmail(email);
		} catch (GzPersistenceException e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			stack.addFlashAttribute("errMsg",stackDump);
			return "redirect:/gz/logon/errStackDump";
		}
	
		model.addAttribute("currUser",currUser);
		
		
		HttpSession session = request.getSession();						// have to set in the session as end up in other controllers
		log.trace("Setting session attribute : sCurrUser : " +  currUser );
		session.setAttribute("sCurrUser", currUser);	
		
		return goAdminHome("",model,stack);
	}
	
	// exec?returnAdmin
	@RequestMapping(value = "/exec", params = "returnAdmin", method = RequestMethod.GET)
    public Object returnAdmin(ModelMap model,HttpServletRequest request,RedirectAttributes stack)
    {
		HttpSession session = request.getSession(false);	
		GzAdmin currUser = (GzAdmin) session.getAttribute("sCurrUser");				
		model.addAttribute("currUser",currUser);
		
		return goAdminHome("",model,stack);
    }
	
	
	@RequestMapping(value = "/exec", params="modifyAdmin", method = RequestMethod.POST)
	public Object modifyAdmin(@ModelAttribute("gzAdminForm") GzAdminForm gzAdminForm,ModelMap model,RedirectAttributes stack)
	{
		GzAdminCommand command = gzAdminForm.getCommand();
		GzProfile profile = command.getProfile();
		
		log.info("Modify profile");
		
		String errMsg = profile.validate(command.getVerifyPassword(),true,GzRole.ROLE_ADMIN); 
		if (errMsg.isEmpty())
		{
			GzAdmin currUser = (GzAdmin) model.get("currUser");
			currUser.copyProfileValues(profile);
			try {
				gzServices.getGzHome().updateAdmin(currUser);
			} catch (GzPersistenceException e) {
				String stackDump = StackDump.toString(e);
				log.error(stackDump);
				stack.addFlashAttribute("errMsg",stackDump);
				return "redirect:/gz/logon/errStackDump";
			}
		}
		
		return goAdminHome(errMsg,model,stack);
	}
	
	private Object goAdminHome(String errMsg,ModelMap model,RedirectAttributes stack){
		
		GzAdminForm adminForm = new GzAdminForm();
		try {
			
			adminForm.setScheduledDownTime(gzServices.getGzHome().getScheduledDownTime());
			adminForm.setDownTimeSet(gzServices.getGzHome().getScheduledDownTimeSet());
			
		} catch (GzPersistenceException e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			stack.addFlashAttribute("errMsg",stackDump);
			return "redirect:/gz/logon/errStackDump";
		}
		
		adminForm.setErrMsg(errMsg);
		return new ModelAndView("adminHome","adminForm", adminForm);
	}
	
	
	@RequestMapping(value = "/scheduleDownTime", method = RequestMethod.GET)
	public Object scheduleDownTime(ModelMap model,RedirectAttributes stack)
	{
		log.info("In scheduleDownTime");
		GzAdminDownTimeForm adminDownTimeForm = getGzAdminDownTimeForm();
		
		return new ModelAndView("adminScheduleDownTime","adminDownTimeForm",adminDownTimeForm);
	}
	
	private GzAdminDownTimeForm getGzAdminDownTimeForm()
	{
		GzAdminDownTimeForm adminDownTimeForm = new GzAdminDownTimeForm();
		try
		{
			GzAdminProperties ap = gzServices.getGzHome().getAdminProperties();
			log.info("Got version code : " + ap.getVersionCode());
			
			adminDownTimeForm.setVersionCode(ap.getVersionCode().toString());
			adminDownTimeForm.setScheduledDownTime(gzServices.getGzHome().getScheduledDownTime());
			adminDownTimeForm.setDownTimeSet(gzServices.getGzHome().getScheduledDownTimeSet());
		}
		catch (GzPersistenceException e)
		{
			String errMsg = "Couldn't get downtime or version code - " + e.getMessage();
			log.error(errMsg);
			adminDownTimeForm.setErrMsg(errMsg);
		}
		return adminDownTimeForm;
	}
	
	@RequestMapping(value = "/exec", params = "submitDownTime", method = RequestMethod.POST)
	public ModelAndView submitDownTime(@ModelAttribute("adminDownTimeForm") GzAdminDownTimeForm adminDownTimeForm,ModelMap model)
	{
		GzAdminDownTimeCommand command = adminDownTimeForm.getCommand();
		String errMsg = "";
		try
		{
			gzServices.getGzHome().storeScheduledDownTime(command.getScheduledDownTime());
			adminDownTimeForm.setInfoMsg("Down time successfully scheduled");
		}
		catch (GzPersistenceException e)
		{
			errMsg = "Couldn't store downtime - " + e.getMessage();
			log.error(errMsg);
			adminDownTimeForm.setErrMsg(errMsg);
		}
		adminDownTimeForm = getGzAdminDownTimeForm();
		if (adminDownTimeForm.getErrMsg()==null || adminDownTimeForm.getErrMsg().isEmpty())
		{
			if (!errMsg.isEmpty())
				adminDownTimeForm.setErrMsg(errMsg);
			else
				adminDownTimeForm.setInfoMsg("Down time successfully scheduled.");
		}
		
		return new ModelAndView("adminScheduleDownTime","adminDownTimeForm",adminDownTimeForm);
	}
	
	@RequestMapping(value = "/exec", params = "exitDownTime", method = RequestMethod.POST)
	public Object exitDownTime(ModelMap model,RedirectAttributes stack)
	{
		return goAdminHome("",model,stack);
	}
	
	@RequestMapping(value = "/exec", params = "cancelDownTime", method = RequestMethod.POST)
	public Object cancelDownTime(ModelMap model)
	{
		String errMsg = "";
		try
		{
			gzServices.getGzHome().setScheduledDownTime(false);
		}
		catch (GzPersistenceException e)
		{
			errMsg = "Couldn't cancel downtime - " + e.getMessage();
			log.error(errMsg);
		}
		GzAdminDownTimeForm adminDownTimeForm = getGzAdminDownTimeForm();
		if (adminDownTimeForm.getErrMsg()==null || adminDownTimeForm.getErrMsg().isEmpty())
		{
			if (!errMsg.isEmpty())
				adminDownTimeForm.setErrMsg(errMsg);
			else
				adminDownTimeForm.setInfoMsg("Down time successfully cancelled.");
		}
		return new ModelAndView("adminScheduleDownTime","adminDownTimeForm",adminDownTimeForm);
	}
	
	@RequestMapping(value = "/exec", params = "updateVersionCode", method = RequestMethod.POST)
	public Object updateVersionCode(@ModelAttribute("adminDownTimeForm") GzAdminDownTimeForm adminDownTimeForm,ModelMap model)
	{
		GzAdminDownTimeCommand command = adminDownTimeForm.getCommand();
		String errMsg = "";
			
		try
		{
			UUID uuid = UUID.fromString(command.getVersionCode());
			gzServices.getGzHome().updateVersionCode(uuid);
		}
		catch (GzPersistenceException | IllegalArgumentException e)
		{
			errMsg = "Couldn't update version code - " + e.getMessage();
			log.error(errMsg);
		}
		adminDownTimeForm = getGzAdminDownTimeForm();
		if (adminDownTimeForm.getErrMsg()==null || adminDownTimeForm.getErrMsg().isEmpty())
		{
			if (!errMsg.isEmpty())
				adminDownTimeForm.setErrMsg(errMsg);
			else
				adminDownTimeForm.setInfoMsg("Version code successfully updated.");
		}
		return new ModelAndView("adminScheduleDownTime","adminDownTimeForm",adminDownTimeForm);
	}
	
	
	@Autowired
	public void setServices(GzServices gzServices)
	{
		this.setGzServices(gzServices);
	}

	public GzServices getGzServices() {
		return gzServices;
	}

	public void setGzServices(GzServices gzServices) {
		this.gzServices = gzServices;
	}
}
