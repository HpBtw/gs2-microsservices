CREATE TABLE IF NOT EXISTS tb_reserva (
                                          id BIGINT NOT NULL AUTO_INCREMENT,
                                          quadra_id BIGINT NOT NULL,
                                          nome_cidadao VARCHAR(120) NOT NULL,
    cpf_cidadao VARCHAR(14) NOT NULL,
    data_reserva DATE NOT NULL,
    codigo_qr_code VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    criado_em DATE NOT NULL,
    atualizado_em DATE,
    CONSTRAINT pk_tb_reserva PRIMARY KEY (id)
    );