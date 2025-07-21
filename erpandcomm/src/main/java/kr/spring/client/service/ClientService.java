package kr.spring.client.service;

import kr.spring.client.vo.ClientVO;
import java.util.List;
import java.util.Map;

public interface ClientService {
    List<ClientVO> getClientList();
    ClientVO getClient(Long client_num);
    void saveClient(ClientVO clientVO);
    void deleteClient(Long client_num);
    List<Map<String, Object>> selectClientSalesStats();
} 