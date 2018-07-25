CREATE TABLE contato(
	codigo BIGSERIAL PRIMARY KEY,
	nome VARCHAR(60) NOT NULL,
	email VARCHAR(60) NOT NULL,
	telefone VARCHAR(40) NOT NULL,
	codigo_pessoa BIGINT NOT NULL,
	FOREIGN KEY(codigo_pessoa) REFERENCES pessoa(codigo)
);

insert into contato(nome, email, telefone, codigo_pessoa) values('Maria Rita', 'mariarita@algamoney.com','3627-1031', 2);

