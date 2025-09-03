package com.ProyectoTingeso1.BackendProyecto1.Services;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ToolDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Kardex;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Tool;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {
    @Autowired
    ToolRepository toolRepository;
    
    public boolean saveTool(Tool tool) {
        int quantity =  tool.getQuantity();
        int i;
        for( i = 0; i < quantity; i++ ) {
            Tool newTool = new Tool();
            Kardex k = new Kardex();

            newTool.setName(tool.getName());
            newTool.setCategory(tool.getCategory());
            newTool.setState(tool.getState());
            newTool.setQuantity(1);
            newTool.setRentDailyRate(tool.getRentDailyRate());
            newTool.setLateFee(tool.getLateFee());
            newTool.setReplacementValue(tool.getReplacementValue());

            k.setTool(newTool);
            //aqui falta codigo
            toolRepository.save(newTool);
        }
        if(i == quantity){

            return true;
        }else{
            return false;
        }
    }
    public List<ToolDTO> getAllTools() {
        return toolRepository.getToolSummary();
    }

    public List<Tool> getTools(){
        return  toolRepository.findAll();
    }

    public Tool getToolById(Long id){
        return toolRepository.findById(id).get();
    }
    public void deleteToolById(Long id){
        toolRepository.deleteById(id);
    }
}
