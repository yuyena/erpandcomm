package kr.spring.client.service;

import kr.spring.client.vo.ClientVO;
import java.util.List;

public interface ClientService {
    List<ClientVO> getClientList();
    ClientVO getClient(Long client_num);
    void saveClient(ClientVO clientVO);
    void deleteClient(Long client_num);
} 