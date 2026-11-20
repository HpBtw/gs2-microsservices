CREATE TABLE IF NOT EXISTS tb_quadra (
                                         id BIGINT NOT NULL AUTO_INCREMENT,
                                         nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(500),
    endereco VARCHAR(200) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    modalidade VARCHAR(50) NOT NULL,
    coberta BOOLEAN NOT NULL,
    iluminacao_noturna BOOLEAN NOT NULL,
    status VARCHAR(50) NOT NULL,
    criado_em DATE NOT NULL,
    atualizado_em DATE NOT NULL,
    CONSTRAINT pk_tb_quadra PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS tb_ocorrencia_zeladoria (
                                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                                       titulo VARCHAR(120) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    foto_url VARCHAR(255),
    solicitante_nome VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_abertura DATE NOT NULL,
    data_resolucao DATE,
    quadra_id BIGINT NOT NULL,
    CONSTRAINT pk_tb_ocorrencia PRIMARY KEY (id),
    CONSTRAINT fk_ocorrencia_quadra FOREIGN KEY (quadra_id) REFERENCES tb_quadra(id)
    );