import os
from dotenv import load_dotenv

# 从 .env 文件中加载环境变量
load_dotenv()

# 数据库配置
DB_CONFIG = {
    'host': os.getenv('DB_HOST'),
    'user': os.getenv('DB_USER'),
    'password': os.getenv('DB_PASSWORD'),
    'database': os.getenv('DB_NAME'),
    'port': os.getenv('DB_PORT')
}

# API 配置
API_CONFIG = {
    'base_url': os.getenv('BASE_URL'),
    'request_delay': os.getenv('REQUEST_DELAY'),
    'timeout': os.getenv('TIMEOUT')
}