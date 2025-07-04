package kr.spring.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import kr.spring.client.vo.ClientVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import kr.spring.client.service.ClientService;

@Controller
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/list")
    public String list(Model model) {
        List<ClientVO> list = clientService.getClientList();
        model.addAttribute("list", list);
        return "views/client/ClientList";
    }

    @GetMapping("/form")
    public String form(@RequestParam(required = false) Long client_num, Model model) {
        ClientVO vo = (client_num != null) ? clientService.getClient(client_num) : new ClientVO();
        model.addAttribute("clientVO", vo);
        return "views/client/ClientForm";
    }

    @PostMapping("/save")
    public String save(ClientVO clientVO) {
        clientService.saveClient(clientVO);
        return "redirect:/client/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long client_num) {
        clientService.deleteClient(client_num);
        return "redirect:/client/list";
    }
} 