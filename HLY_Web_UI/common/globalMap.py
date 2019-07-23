import json

__author__ = 'yuezy'


class GlobalMap:  # 跨文件全局变量

    map = {}

    def set(self, key, value):
        if isinstance(value, dict):
            value = json.dumps(value)
        self.map[key] = value

    def get(self, key):
        return self.map[key]
