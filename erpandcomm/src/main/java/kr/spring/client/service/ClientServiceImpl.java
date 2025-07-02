package kr.spring.client.service;

import org.springframework.stereotype.Service;
import kr.spring.client.vo.ClientVO;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Override
    public List<ClientVO> getClientList() { return null; }
    @Override
    public ClientVO getClient(Long client_num) { return null; }
    @Override
    public void saveClient(ClientVO clientVO) {}
    @Override
    public void deleteClient(Long client_num) {}
} 