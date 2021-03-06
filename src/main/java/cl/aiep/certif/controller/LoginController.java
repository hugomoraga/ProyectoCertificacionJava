package cl.aiep.certif.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import cl.aiep.certif.controller.service.CursoService;
import cl.aiep.certif.controller.service.EstudianteService;
import cl.aiep.certif.dao.dto.CursoDTO;
import cl.aiep.certif.dao.dto.EstudianteDTO;
import cl.aiep.certif.util.CommonUtil;

@Controller
//@PreAuthorize("isAuthenticated()")
public class LoginController {

	@Autowired
	EstudianteService serviceEst;

	@Autowired
	CursoService serviceCurso;

	@GetMapping("/login")
	public String viewLoginPage() {
		return "login";
	}

	@GetMapping("/salir")
	public String viewLogoutPage() {
		return "redirect:/";
	}

	@GetMapping("/registrate")
	public String viewRegister(final Model model) {
		model.addAttribute("estudiante", new EstudianteDTO());
		model.addAttribute("regiones", serviceEst.obtienRegiones());
		return "nuevo";

	}

	@PostMapping("/guardar")
	public String guardar(@Valid EstudianteDTO estudiante, BindingResult result, final Model model) {
		if (result.hasErrors()) {
			model.addAttribute("estudiante", estudiante);
			model.addAttribute("regiones", serviceEst.obtienRegiones());
			model.addAttribute("mensaje", result.getFieldError().getDefaultMessage());
			return "nuevo";
		} else {

		}

		if (CommonUtil.validarRut(estudiante.getRut()))
			serviceEst.guardaEstudiante(estudiante);
		else {
			model.addAttribute("estudiante", estudiante);
			model.addAttribute("regiones", serviceEst.obtienRegiones());
			model.addAttribute("mensaje", "Rut Invalido");
			return "nuevo";

		}
		return "redirect:/dashboard/estudiante";

	}


	@GetMapping("/")
	public String indexCursos(final Model model) {
		List cursos = serviceCurso.obtenerCursos();
		cursos.remove(0);
		model.addAttribute("cursos", cursos);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("mensaje");
		if (auth.getPrincipal().equals("anonymousUser")) {
			EstudianteDTO estudianteNull = new EstudianteDTO();
			model.addAttribute("estudiante", estudianteNull);	
			return "home";		
		}
		
	
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	@PreAuthorize("isAuthenticated()")
	public String indexCursosAuth(final Model model) {
		List cursos = serviceCurso.obtenerCursos();
		cursos.remove(0);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getPrincipal();
		EstudianteDTO estudiante = serviceEst.obtenerEstudiante(user.getUsername());
		model.addAttribute("estudiante", estudiante);
		model.addAttribute("cursos", cursos);
		model.addAttribute("mensaje");
		return "home";
	}

	@GetMapping("/dashboard/admin")
	@PreAuthorize("isAuthenticated()")
	public String adminCursos(final Model model) {
		model.addAttribute("cursos", serviceCurso.obtenerCursos());
		return "dashboardAdmin";
	}

	
	@GetMapping("/dashboard/estudiante")
	@PreAuthorize("isAuthenticated()")
	public String panelUsuario(final Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object user =  auth.getPrincipal();
		CursoDTO curso = serviceEst.obtenerCurso(auth.getName());
		EstudianteDTO estudiante = serviceEst.obtenerEstudiante(((User) user).getUsername());
		model.addAttribute("estudiante", estudiante);
		model.addAttribute("curso", curso );
		
		model.addAttribute("contenido", serviceCurso.obtenerContenidos(curso.getId()));

		return "dashboardEstudiante";
		
	}

	@GetMapping("/dashboard")
	@PreAuthorize("isAuthenticated()")
    public String miPanel(final Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        if (role.contains("ADMIN")) {
            return "redirect:/dashboard/admin";

        } else  {
        	return "redirect:/dashboard/estudiante";
    }
	}
	
	
}
