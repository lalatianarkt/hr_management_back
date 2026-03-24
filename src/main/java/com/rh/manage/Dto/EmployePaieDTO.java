package com.rh.manage.Dto;

public class EmployePaieDTO {
    EmployeInfosDTO employeAvecInfosDTO;
    int etat;

    public int getEtat() {return etat;}
    public void setEtat(int etat) {this.etat = etat;}
    public EmployeInfosDTO getEmployeAvecInfosDTO() {return employeAvecInfosDTO;}
    public void setEmployeAvecInfosDTO(EmployeInfosDTO employeAvecInfosDTO) {this.employeAvecInfosDTO = employeAvecInfosDTO;}
    
}
