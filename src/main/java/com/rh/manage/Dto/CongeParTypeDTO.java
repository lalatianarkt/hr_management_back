package com.rh.manage.Dto;

import com.rh.manage.Model.TypeConge;

public class CongeParTypeDTO {
    // const congesParType = [
    // { type: 'Congé annuel', count: 65, pourcentage: 45.8, color: '#4CAF50' },
    // { type: 'Congé maladie', count: 32, pourcentage: 22.5, color: '#2196F3' },
    // { type: 'RTT', count: 18, pourcentage: 12.7, color: '#FF9800' },
    // { type: 'Congé maternité', count: 12, pourcentage: 8.5, color: '#E91E63' },
    // { type: 'Congé paternité', count: 8, pourcentage: 5.6, color: '#9C27B0' },
    // { type: 'Autre motif', count: 7, pourcentage: 4.9, color: '#795548' }
//   ];
    TypeConge typeConge;
    Long count;
    Double pourcentage;
    String color;
    
    public TypeConge getTypeConge() {
        return typeConge;
    }
    public void setTypeConge(TypeConge typeConge) {
        this.typeConge = typeConge;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public Double getPourcentage() {
        return pourcentage;
    }
    public void setPourcentage(Double pourcentage) {
        this.pourcentage = pourcentage;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    
}
