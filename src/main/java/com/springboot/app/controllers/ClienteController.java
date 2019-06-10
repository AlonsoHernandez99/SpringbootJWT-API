package com.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.models.entity.Cliente;
import com.springboot.app.models.service.IClienteService;
import com.springboot.app.models.service.IUploadFileService;
import com.springboot.app.utils.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/listar-rest")
	@ResponseBody
	public List<Cliente> listarRest() {
		return clienteService.findAll();
	}

	@RequestMapping(path = { "/listar", "/" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") Integer page, Model model,
			Authentication authentication, HttpServletRequest request,Locale locale) {

		if (authentication != null) {
			log.info("Usuario autenticado con el nombre de :" + authentication.getName());
			
			//Verficando el rol del usuario autenticado
			SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(
					request, "");
			if (securityContext.isUserInRole("ROLE_ADMIN")) {
				log.info("Hola: " + authentication.getName() + " tienes acceso.");
			} else {
				log.info("Hola: " + authentication.getName() + " NO tienes acceso.");
			}
		}

		// Para resultados paginados
		Pageable pageRequest = PageRequest.of(page, 4);

		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		// Creando objeto para la paginación
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo",null,locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";

	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String showForm(Model model) {

		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Formulario de Cliente");
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String saveCliente(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam(name = "file") MultipartFile foto, SessionStatus status, RedirectAttributes flash) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		if (!foto.isEmpty()) {
			// Path directorio = Paths.get("src//main//resources//static//uploads");
			// String rootPath = "C://uploads";

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {

				uploadFileService.deleteFile(cliente.getFoto());
			}

			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copyFile(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Imagen subida correctamente: " + uniqueFileName);

			// Dando el nombre de la imagen al cliente
			cliente.setFoto(uniqueFileName);
		}
		String mensaje = (cliente.getId() != null) ? "Cliente editado correctamente"
				: "Cliente registrado correctamente";

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String edit(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findById(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El Cliente no existe en la BD!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El Id del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Editar Cliente");
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Cliente cliente = clienteService.findById(id);

			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado correctamente");

			if (uploadFileService.deleteFile(cliente.getFoto()))
				flash.addFlashAttribute("info", "Foto: " + cliente.getFoto() + " eliminada con exito!");
		}
		return "redirect:/listar";
	}
	
	@Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String verDatosCliente(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Cliente cliente = clienteService.fetchByIdWithFacturas(id);

		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la BD");
			return "redirect:/listar";
		}
		model.addAttribute("cliente", cliente);
		model.addAttribute("titulo", "Detalle de cliente: " + cliente.getNombre());
		return "ver";
	}
	
	@Secured("ROLE_USER")
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable(value = "filename") String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.loadFile(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
}
