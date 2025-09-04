-- V5__insert_admin.sql
-- Insere usuário admin padrão se ainda não existir
-- Senha: admin (sem criptografia, usado só no profile DEV)

INSERT INTO users (username, password, role)
SELECT 'admin', 'admin', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');
