package kr.spring.client.service;

import org.springframework.stereotype.Service;
import kr.spring.client.vo.ClientVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import kr.spring.client.dao.ClientMapper;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientMapper clientMapper;

    @Override
    public List<ClientVO> getClientList() {
        return clientMapper.selectClientList();
    }
    @Override
    public ClientVO getClient(Long client_num) {
        return clientMapper.selectClient(client_num);
    }
    @Override
    public void saveClient(ClientVO clientVO) {
        if (clientVO.getClient_num() == 0) {
            clientMapper.insertClient(clientVO);
        } else {
            clientMapper.updateClient(clientVO);
        }
    }
    @Override
    public void deleteClient(Long client_num) {
        clientMapper.deleteClient(client_num);
    }
} 