#!/bin/bash
echo "Setting up MySQL Replication..."

# -------------------------
# 1️⃣ Master 설정
# -------------------------
echo "Configuring Master..."

docker exec mysql-master mysql -uroot -prootpassword -e "
-- replication 계정
DROP USER IF EXISTS 'repl'@'%';
CREATE USER 'repl'@'%' IDENTIFIED WITH mysql_native_password BY 'repl_password';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';

-- jobtalk_user 계정
DROP USER IF EXISTS 'jobtalk_user'@'%';
DROP USER IF EXISTS 'jobtalk_user'@'localhost';
DROP USER IF EXISTS 'jobtalk_user'@'127.0.0.1';

CREATE USER IF NOT EXISTS 'jobtalk_user'@'%' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';
CREATE USER IF NOT EXISTS 'jobtalk_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';
CREATE USER IF NOT EXISTS 'jobtalk_user'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';

ALTER USER 'jobtalk_user'@'%' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';
ALTER USER 'jobtalk_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';
ALTER USER 'jobtalk_user'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';

GRANT ALL PRIVILEGES ON jobtalk_db.* TO 'jobtalk_user'@'%';
GRANT ALL PRIVILEGES ON jobtalk_db.* TO 'jobtalk_user'@'localhost';
GRANT ALL PRIVILEGES ON jobtalk_db.* TO 'jobtalk_user'@'127.0.0.1';

FLUSH PRIVILEGES;
"

# -------------------------
# 2️⃣ Master 상태 확인
# -------------------------
echo "Getting Master status..."
MASTER_STATUS=$(docker exec mysql-master mysql -uroot -prootpassword -e "SHOW MASTER STATUS;" | tail -1)
LOG_FILE=$(echo $MASTER_STATUS | awk '{print $1}')
LOG_POS=$(echo $MASTER_STATUS | awk '{print $2}')

echo "Master Log File: $LOG_FILE"
echo "Master Log Position: $LOG_POS"

# -------------------------
# 3️⃣ Slave 설정
# -------------------------
echo "Configuring Slave..."

docker exec mysql-slave mysql -uroot -prootpassword -e "
-- replication 계정
STOP SLAVE;
RESET SLAVE ALL;
CHANGE MASTER TO
    MASTER_HOST='mysql-master',
    MASTER_USER='repl',
    MASTER_PASSWORD='repl_password',
    MASTER_LOG_FILE='$LOG_FILE',
    MASTER_LOG_POS=$LOG_POS;
START SLAVE;

-- jobtalk_user 계정
DROP USER IF EXISTS 'jobtalk_user'@'%';
DROP USER IF EXISTS 'jobtalk_user'@'localhost';
DROP USER IF EXISTS 'jobtalk_user'@'127.0.0.1';

CREATE USER IF NOT EXISTS 'jobtalk_user'@'%' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';
CREATE USER IF NOT EXISTS 'jobtalk_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';
CREATE USER IF NOT EXISTS 'jobtalk_user'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';

ALTER USER 'jobtalk_user'@'%' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';
ALTER USER 'jobtalk_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';
ALTER USER 'jobtalk_user'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY 'jobtalk_password';

GRANT ALL PRIVILEGES ON jobtalk_db.* TO 'jobtalk_user'@'%';
GRANT ALL PRIVILEGES ON jobtalk_db.* TO 'jobtalk_user'@'localhost';
GRANT ALL PRIVILEGES ON jobtalk_db.* TO 'jobtalk_user'@'127.0.0.1';

FLUSH PRIVILEGES;
"

# -------------------------
# 4️⃣ 대기 후 Slave 상태 확인
# -------------------------
echo "Waiting for replication to start..."
sleep 5

echo "Checking Slave status..."
docker exec mysql-slave mysql -uroot -prootpassword -e "SHOW SLAVE STATUS\G" | grep -E "(Slave_IO_Running|Slave_SQL_Running)"

echo "Replication setup complete!"
echo "If both values show 'Yes', replication is working correctly."