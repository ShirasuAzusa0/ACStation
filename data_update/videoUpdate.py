import requests
import time
import logging
import pymysql
from pymysql.cursors import DictCursor
from typing import List, Dict, Any, Optional

from config import DB_CONFIG

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('video_update.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

class dataUpdater:
    def __init__(self, db_config):
        self.db_config = db_config
        self.api_base = "https://api.bilibili.com/x/web-interface/view?bvid="
        self.headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Referer": "https://www.bilibili.com",
            "Accept": "application/json, text/plain, */*"
        }

    # 连接数据库
    def get_db_connection(self):
        try:
            connection = pymysql.connect(
                host=self.db_config['host'],
                user=self.db_config['user'],
                password=self.db_config['password'],
                database=self.db_config['database'],
                charset='utf8mb4',
                cursorclass=DictCursor
            )
            return connection
        except Exception as e:
            logger.error(f"数据库连接失败: {e}")
            raise

    # 获取数据库中要更新的所有数据
    def get_all_data(self) -> List[Dict[str, Any]]:
        connection = self.get_db_connection()
        try:
            with connection.cursor() as cursor:
                sql = """
                SELECT
                    videoId, BVid, videoTitle, videoAvatar, views, description
                FROM
                    videos
                WHERE
                    BVid IS NOT NULL AND BVid != 'none'
                """
                cursor.execute(sql)
                videos = cursor.fetchall()
                logger.info(f"获取到 {len(videos)} 个需要更新的视频")
                return videos
        except Exception as e:
            logger.error(f"获取视频列表失败: {e}")
            return []
        finally:
            connection.close()

    # 获取最新的视频信息
    def fetch_video_info(self, BVid:str) -> Optional[Dict[str, Any]]:
        url = self.api_base + BVid
        try:
            # 禁用系统代理
            proxies = {"http": None, "https": None}
            resp = requests.get(url, headers=self.headers, timeout=10, proxies=proxies)
            resp.raise_for_status()
            data = resp.json()
            if data.get("code") != 0:
                logger.warning(f"BVid {BVid} 请求失败: {data.get('message')}")
                return None
            info = data["data"]
            return {
                "title": info.get("title"),
                "views": info.get("stat", {}).get("view"),
                "cover": info.get("pic"),
                "desc": info.get("desc")
            }
        except Exception as e:
            logger.error(f"请求 BVid={BVid} 失败: {e}")
            return None

    # 更新数据
    def update_video_in_db(self, video_id: int, new_info: Dict[str, Any]):
        connection = self.get_db_connection()
        try:
            with connection.cursor() as cursor:
                sql = """
                        UPDATE videos
                        SET videoTitle = %s, views = %s, videoAvatar = %s, description = %s
                        WHERE videoId = %s
                      """
                cursor.execute(sql, (
                    new_info["title"],
                    new_info["views"],
                    new_info["cover"],
                    new_info["desc"],
                    video_id
                ))
            connection.commit()
            logger.info(f"视频(id={video_id}) 数据库已更新。")
        except Exception as e:
            logger.error(f"更新视频(id={video_id}) 失败: {e}")
            connection.rollback()
        finally:
            connection.close()

    # 逐个更新数据
    def run(self, delay: float = 2.0):
        videos = self.get_all_data()
        for video in videos:
            BVid = video["BVid"]
            logger.info(f"正在更新视频: {BVid}")

            new_info = self.fetch_video_info(BVid)
            if not new_info:
                continue

            # 对比是否有变化
            if (
                    video["videoTitle"] == new_info["title"]
                    and video["views"] == new_info["views"]
                    and video["videoAvatar"] == new_info["cover"]
                    and video["description"] == new_info["desc"]
            ):
                logger.info(f"视频 {BVid} 无变化，跳过。")
                continue

            # 更新数据库
            self.update_video_in_db(video["videoId"], new_info)

            # 请求间隔
            time.sleep(delay)

# 运行数据更新自动化脚本
if __name__ == "__main__":
    updater = dataUpdater(DB_CONFIG)
    while True:
        logger.info("开始执行每日视频更新任务")
        updater.run(delay=2.0)
        logger.info("本次任务完成，进入休眠 24 小时")
        time.sleep(24 * 60 * 60)