$(function(){
    $(document).on('click', '.delete-btn', function(){
        if(!confirm('정말 삭제하시겠습니까?')) return;
        var roomNum = $(this).data('room-id');
        $.ajax({
            url:'notActiveRoom',
            type:'post',
            data: { room_num: roomNum },
            dataType:'json',
            beforeSend: function(xhr){
                xhr.setRequestHeader(
                    $('meta[name="csrf-header"]').attr('content'),
                    $('meta[name="csrf-token"]').attr('content')
                );
            },
            success:function(param){
                if(param.result == 'logout'){
                    alert('로그인 후 사용하세요');
                }else if(param.result == 'success'){
                    alert('채팅방이 비활성화되었습니다.');
                    location.reload();
                }else if(param.result == 'wrongAccess'){
                    alert('방장만 삭제할 수 있습니다.');
                }else{
                    alert('삭제 오류');
                }
            },
            error:function(){
                alert('네트워크 오류 발생');
            }
        });
    });
});