package cl.aiep.certif.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cl.aiep.certif.controller.service.CursoService;
import cl.aiep.certif.dao.ICursosDAO;
import cl.aiep.certif.dao.dto.ContenidoDTO;
import cl.aiep.certif.dao.dto.CursoDTO;
import cl.aiep.certif.dao.dto.EstudianteDTO;
import cl.aiep.certif.dao.entity.Contenido;
import cl.aiep.certif.dao.entity.Curso;
import cl.aiep.certif.dao.entity.Estudiante;
import cl.aiep.certif.dao.repository.ContenidoRepository;
import cl.aiep.certif.dao.repository.CursoRepository;
import cl.aiep.certif.dao.repository.EstudianteRepository;

@Repository
public class CursosDAOImpl implements ICursosDAO{
	
	
	@Autowired
	CursoRepository repoCurso;
	
	@Autowired
	ContenidoRepository repoContenido;
	
	@Autowired
	EstudianteRepository estRepo;
	
	@Autowired
	CursoService cursoService;

	@Override 
	public List<CursoDTO> obtenerCursos() {
		List<CursoDTO> retorno= new ArrayList<CursoDTO>();
		for(Curso curso:repoCurso.findAll())
			retorno.add(new CursoDTO(curso.getId(), curso.getNombre(),
					curso.getImagen(), curso.getFecinicio(),
					curso.getFectermino(), curso.getCupos(), curso.getDescripcion() )
					);
		for( int i = 0; i<retorno.size(); i++) {
			retorno.get(i).setInscripcion(cursoService.getNumeroInscritos(retorno.get(i).getId()));
		}
		
		return retorno;
	}

	@Override
	public CursoDTO obtenerCurso(Integer id) {
		Optional<Curso> optional = repoCurso.findById(id);
		Curso curso = new Curso();
		if(optional.isPresent())
			curso = (Curso)optional.get();
			
		return new CursoDTO(curso.getId(), curso.getNombre(),
				curso.getImagen(), curso.getFecinicio(),
				curso.getFectermino(), curso.getCupos(), curso.getDescripcion() );
	}

	@Override
	public boolean guardarCurso(CursoDTO curso) {
		boolean retorno= true;
		try {
		repoCurso.save(new Curso(curso.getNombre(), curso.getImagen(),
				new Date(curso.getFecinicio().getTime()),new Date(curso.getFectermino().getTime()),curso.getCupos(),
				curso.getDescripcion()));
		}catch (Exception e) {
			retorno=false;
		}
		
		return retorno;
	}

	@Override
	public boolean actualizarCurso(CursoDTO curso) {
		boolean retorno= true;
		try {
		repoCurso.save(new Curso(curso.getId(), curso.getNombre(), curso.getImagen(),
				new Date(curso.getFecinicio().getTime()),new Date(curso.getFectermino().getTime()),curso.getCupos(),
				curso.getDescripcion()));
		}catch (Exception e) {
			e.printStackTrace();
			retorno=false;
		}
		
		return retorno;
	}

	@Override
	public boolean eliminarCurso(Integer id) {
		boolean retorno=true;
		try {
		   for(Estudiante est:estRepo.findByCurso(repoCurso.getById(id))) {
				est.setCurso(repoCurso.getById(0));
				estRepo.save(est);
				}
			
		repoCurso.deleteById(id);
		}catch (Exception e) {
			retorno= false;
		}
		return retorno;
	}

	@Override
	public boolean tieneCupos(Integer id) {
		Curso cur= repoCurso.getById(id);
		Integer inscritos= (int) estRepo.countByCurso(cur);
		Integer cupos = cur.getCupos();
		if (cupos>inscritos) {
			return true;
		} else
			return false;
		
	}

	@Override
	public boolean guardarContenido(ContenidoDTO contenido) {
		boolean retorno=true;
		try {
		repoContenido.save(new Contenido(contenido.getNombre(), 
				contenido.getDetalle(),
				repoCurso.getById(contenido.getIdCurso()) ));
		}catch (Exception e) {
			retorno=false;
		}
		return retorno;
	}

	@Override
	public boolean actualizarContenido(ContenidoDTO contenido) {
		boolean retorno=true;
		try {
		repoContenido.save(new Contenido(contenido.getId(), contenido.getNombre(), 
				contenido.getDetalle(), contenido.getIdCurso()));
		}catch (Exception e) {
			retorno=false;
		}
		return retorno;
	}

	@Override
	public boolean eliminarContenido(Integer id) {
		boolean retorno=true;
		try {
		repoContenido.deleteById(id);
		}catch (Exception e) {
			retorno=false;
		}
		return retorno;
	}

	@Override
	public List<ContenidoDTO> obtieneContenidos(Integer id) {
		Set<Contenido> contenido= repoCurso.getById(id).getContenido();
		List<ContenidoDTO> retorno= new ArrayList<ContenidoDTO>();
		for(Contenido cont: contenido)
			retorno.add(new ContenidoDTO(cont.getId(),cont.getNombre(), cont.getDetalle(), cont.getIdCurso()));
		
		return retorno;
	}

	@Override
	public Integer getNumeroInscritos(Integer id) {
		Curso curso= repoCurso.getById(id);
		
		long cantidad= estRepo.countByCurso(curso);
		System.out.println(cantidad);
		return (int) cantidad;
		
	}


	
	

}
