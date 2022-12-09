package com.mycompany.erp.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.mycompany.erp.model.Empresa;
import com.mycompany.erp.model.RamoAtividade;
import com.mycompany.erp.model.TipoEmpresa;
import com.mycompany.erp.repository.Empresas;
import com.mycompany.erp.repository.RamoAtividades;
import com.mycompany.erp.service.CadastroEmpresaService;
import com.mycompany.erp.util.FacesMessages;

@Named
@ViewScoped
public class GestaoEmpresasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Empresas empresas;
	
	@Inject
	private FacesMessages messages;
	
	@Inject
	private RamoAtividades ramoAtividades;
	
	@Inject
	private CadastroEmpresaService cadastroEmpresaService;
	
	private List<Empresa> listaEmpresas;
	
	private String termoPesquisa;
	
	private Converter ramoAtividadeConverter;
	
	private Empresa empresa;
	
	public void prepararNovaEmpresa() {
		empresa = new Empresa();
	}
	
	public void prepararEdicao() {
		ramoAtividadeConverter = new RamoAtividadeConverter(Arrays.asList(empresa.getRamoAtividade()));
	}
	
	public void salvar() {
		cadastroEmpresaService.salvar(empresa);
		
		atualizarRegistros();
		
		messages.info("Empresa salva com sucseso!");
		
		RequestContext.getCurrentInstance().update(Arrays.asList("frm:empresasDataTable", "frm:messages"));
	}
	
	public void excluir() {
		cadastroEmpresaService.excluir(empresa);
		
		empresa = null;
		
		atualizarRegistros();
		
		messages.info("Empresa excluída com sucesso!");
	}
	
	public void pesquisar() {
		listaEmpresas = empresas.pesquisar(termoPesquisa);
		
		if (listaEmpresas.isEmpty()) {
			messages.info("Sua consulta não retornou registros.");
		}
	}

	public void todasEmpresas() {
		listaEmpresas = empresas.todas();
	}
	
	public List<RamoAtividade> completarRamoAtividade(String termo){
		List<RamoAtividade> listaRamoAtividades = ramoAtividades.pesquisar(termo);
		
		ramoAtividadeConverter = new RamoAtividadeConverter(listaRamoAtividades);
		
		return listaRamoAtividades;
	}
	
	private void atualizarRegistros() {
		if(jaHouvePesquisa()) {
			pesquisar();
		} else {
			todasEmpresas();
		}
	}
	
	private boolean jaHouvePesquisa() {
		return termoPesquisa != null && !"".equals(termoPesquisa);
	}
	
	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}
	
	public String getTermoPesquisa() {
		return termoPesquisa;
	}
	
	public void setTermoPesquisa(String termoPesquisa) {
		this.termoPesquisa = termoPesquisa;
	}
	
	public TipoEmpresa[] getTiposEmpresa() {
		return TipoEmpresa.values();
	}
	
	public Converter getRamoAtividadeConverter() {
		return ramoAtividadeConverter;
	}
	
	public void setRamoAtividadeConverter(Converter ramoAtividadeConverter) {
		this.ramoAtividadeConverter = ramoAtividadeConverter;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public boolean isEmpresaSelecionada() {
		return empresa != null && empresa.getId() != null;
	}
}
