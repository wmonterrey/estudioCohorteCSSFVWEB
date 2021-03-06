package ni.com.sts.estudioCohorteCssfv.datos.hojaConsulta;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.EstadosHoja;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCssfv.servicios.HojaConsultaService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;

public class HojaConsultaDA implements HojaConsultaService {

	private static final HibernateResource hibernateResource = new HibernateResource();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HojaConsulta> getHojasConsultaPendientesCarga() throws Exception {
		List<HojaConsulta> resultado = new ArrayList<HojaConsulta>();
		
		try{
			String sql = "select hc from HojaConsulta hc "+
							"where hc.estado = :estado "+
							"and (hc.estadoCarga = :estadoCarga or hc.estadoCarga is null)";
			
			Query q = hibernateResource.getSession().createQuery(sql);
			q.setCharacter("estado", '7');
			q.setParameter("estadoCarga", '0');
   
   			resultado = q.list();
		} catch (Exception e) {
		 		e.printStackTrace();
		 		throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}	
        }
        return resultado;
	}

	public void updateHojaConsulta(HojaConsulta hoja) throws Exception{
		try {
			hibernateResource.begin();
            hibernateResource.getSession().update(hoja);
            hibernateResource.getSession().flush();
            hibernateResource.commit();
        } catch (Exception e) {
            e.printStackTrace();
            hibernateResource.rollback();
        } finally {
            if (hibernateResource.getSession().isOpen()) {
                hibernateResource.close();
            }
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EstadosHoja> getEstadosHojaConsulta() throws Exception {
		List<EstadosHoja> resultado = new ArrayList<EstadosHoja>();
		
		try{
			String sql = "select eh from EstadosHoja eh "+
							"where eh.estado = :estado order by eh.codigo asc";
			
			Query q = hibernateResource.getSession().createQuery(sql);
			q.setCharacter("estado", '1');
   
   			resultado = q.list();
		} catch (Exception e) {
		 		e.printStackTrace();
		 		throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}	
        }
        return resultado;
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public List<HojaConsulta> getHojaConsultaActivaHoyByCodExpediente(int codExpediente) throws Exception{
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		try {
           // Construir query
   	 	String sql = "select hc from HojaConsulta hc "+
				"where hc.estado not in ('7','8') "+ //cerrado o abandono
				"and hc.codExpediente = :codExpediente " +
				"and to_char(hc.fechaConsulta,'ddMMyyyy') = to_char(CURRENT_DATE,'ddMMyyyy') ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("codExpediente",  codExpediente);
           
           lista = (List<HojaConsulta>) q.list();

   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 		throw new Exception(e);
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

   	 	return lista;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public HojaConsulta getHojaConsultaActivaByCodExpediente(int codExpediente) throws Exception{
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		HojaConsulta resultado = null;
		try {
           // Construir query
   	 	String sql = "select hc from HojaConsulta hc "+
				"where hc.codExpediente = :codExpediente order by hc.fechaConsulta desc";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("codExpediente",  codExpediente);
           
           lista = (List<HojaConsulta>) q.list();
           if (lista!=null && lista.size()>0)
        	   resultado = lista.get(0);

   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 		throw new Exception(e);
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}
   	 	return resultado;	
	}
	
	@Override
    public HojaConsulta getHojaConsultaByNumHoja(int numHoja) throws Exception{
		HojaConsulta resultado = null;
		try {
           // Construir query
   	 	String sql = "select hc from HojaConsulta hc "+
				"where hc.numHojaConsulta = :numHoja";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("numHoja",  numHoja);
           
           resultado = (HojaConsulta)q.uniqueResult();

   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 		throw new Exception(e);
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}
   	 	return resultado;	
	}

	@SuppressWarnings("unchecked")
	@Override
    public List<HojaConsulta> getHojaConsultaActivaAndAdmiPenByCodExp(int codExpediente) throws Exception{
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		try {
           // Construir query
   	 	String sql = "select hc from Admision ad, HojaConsulta hc "+
				"where ad.numHojaConsulta = hc.numHojaConsulta and ad.codExpediente = hc.codExpediente " +
				"and hc.estado not in ('7','8') "+ //cerrado o abandono
				"and hc.codExpediente = :codExpediente  and ad.fechaEntrada is null";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("codExpediente",  codExpediente);
           
           lista = (List<HojaConsulta>) q.list();

   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 		throw new Exception(e);
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

   	 	return lista;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public List<HojaConsulta> getHojaConsultaNoActivaAndAdmiPenByCodExp(int codExpediente) throws Exception{
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		try {
           // Construir query
   	 	String sql = "select hc from Admision ad, HojaConsulta hc "+
				"where ad.numHojaConsulta = hc.numHojaConsulta and ad.codExpediente = hc.codExpediente " +
				"and hc.estado  in ('7','8') "+ //cerrado o abandono
				"and hc.codExpediente = :codExpediente  and ad.fechaEntrada is null";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("codExpediente",  codExpediente);
           
           lista = (List<HojaConsulta>) q.list();

   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 		throw new Exception(e);
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

   	 	return lista;	
	}
	
	@Override
	public EstadosHoja getEstadoHojaConsultaByNumHoja(int numHoja) throws Exception{
		EstadosHoja resultado = null;
		try {
           // Construir query
   	 	String sql = "select eh from HojaConsulta hc, EstadosHoja eh "+
				"where eh.codigo = hc.estado and hc.numHojaConsulta = :numHoja";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("numHoja",  numHoja);
           
           resultado = (EstadosHoja)q.uniqueResult();

   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 		throw new Exception(e);
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}
   	 	return resultado;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public List<HojaConsulta> getHojasConsultaSinAdmision(int codExpediente) throws Exception{
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		try {
           // Construir query
   	 	String sql = "select hc from HojaConsulta hc "+
				"where hc.codExpediente = :codExpediente and hc.numHojaConsulta not in (select numHojaConsulta from Admision) "
				+ "order by hc.fechaConsulta desc";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("codExpediente",  codExpediente);
           
           lista = (List<HojaConsulta>) q.list();
   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 		throw new Exception(e);
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}
   	 	return lista;	
	}
}
