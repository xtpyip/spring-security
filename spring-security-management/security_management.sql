/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : security_management

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 30/07/2024 17:15:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins`  (
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `series` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of persistent_logins
-- ----------------------------

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `permission_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限名称',
  `permission_tag` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标签',
  `permission_url` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES (1, '查询所有用户', 'user:findAll', '/user/findAll');
INSERT INTO `t_permission` VALUES (2, '用户添加或修改', 'user:saveOrUpdate', '/user/saveOrUpdate');
INSERT INTO `t_permission` VALUES (3, '用户删除', 'user:delete', '/delete/{id}');
INSERT INTO `t_permission` VALUES (4, '根据ID查询用户', 'user:getById', '/user/{id}');
INSERT INTO `t_permission` VALUES (5, '查询所有商品', 'product:findAll', '/product/findAll');
INSERT INTO `t_permission` VALUES (6, '商品添加或修改', 'product:saveOrUpdate', '/product/saveOrUpdate');
INSERT INTO `t_permission` VALUES (7, '商品删除', 'product:delete', '/product/delete/{id}');
INSERT INTO `t_permission` VALUES (8, '商品是否显示', 'product:show', '/product/show/{isShow}');

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `stock` int NULL DEFAULT NULL COMMENT '库存',
  `is_show` tinyint NULL DEFAULT NULL COMMENT '是否展示',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES (1, '华为mate20', 1700.00, 40, 0, '2024-07-30 09:04:39');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_desc` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'ADMIN', '超级管理员');
INSERT INTO `t_role` VALUES (2, 'USER', '用户管理');
INSERT INTO `t_role` VALUES (3, 'PRODUCT', '商品管理员');
INSERT INTO `t_role` VALUES (4, 'PRODUCT_INPUT', '商品录入员');
INSERT INTO `t_role` VALUES (5, 'PRODUCT_SHOW', '商品审核员');

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`  (
  `rid` int NOT NULL COMMENT '角色编号',
  `pid` int NOT NULL COMMENT '权限编号',
  PRIMARY KEY (`rid`, `pid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES (1, 1);
INSERT INTO `t_role_permission` VALUES (1, 2);
INSERT INTO `t_role_permission` VALUES (1, 3);
INSERT INTO `t_role_permission` VALUES (1, 4);
INSERT INTO `t_role_permission` VALUES (1, 5);
INSERT INTO `t_role_permission` VALUES (1, 6);
INSERT INTO `t_role_permission` VALUES (1, 7);
INSERT INTO `t_role_permission` VALUES (1, 8);
INSERT INTO `t_role_permission` VALUES (2, 1);
INSERT INTO `t_role_permission` VALUES (2, 2);
INSERT INTO `t_role_permission` VALUES (2, 3);
INSERT INTO `t_role_permission` VALUES (2, 4);
INSERT INTO `t_role_permission` VALUES (3, 5);
INSERT INTO `t_role_permission` VALUES (3, 6);
INSERT INTO `t_role_permission` VALUES (3, 7);
INSERT INTO `t_role_permission` VALUES (3, 8);
INSERT INTO `t_role_permission` VALUES (4, 5);
INSERT INTO `t_role_permission` VALUES (4, 6);
INSERT INTO `t_role_permission` VALUES (4, 7);
INSERT INTO `t_role_permission` VALUES (5, 5);
INSERT INTO `t_role_permission` VALUES (5, 8);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `status` tinyint NULL DEFAULT NULL COMMENT '用户状态，1启用，0停用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'admin', '$2a$10$X2ORrRFAhlLWUwozlxBB8eo4LKvmoWSu6eHxJNEOmyf5k3oOtq2x2', 1);
INSERT INTO `t_user` VALUES (2, 'zhaoyang', '$2a$10$zF6j11V49w2jVlzMbImmhuUv8qVmigepCJ4cbPzTsnr4mZEcUw/ua', 1);
INSERT INTO `t_user` VALUES (3, 'user1', '$2a$10$zF6j11V49w2jVlzMbImmhuUv8qVmigepCJ4cbPzTsnr4mZEcUw/ua', 1);
INSERT INTO `t_user` VALUES (4, 'user2', '$2a$10$zF6j11V49w2jVlzMbImmhuUv8qVmigepCJ4cbPzTsnr4mZEcUw/ua', 1);
INSERT INTO `t_user` VALUES (5, 'user3', '$2a$10$zF6j11V49w2jVlzMbImmhuUv8qVmigepCJ4cbPzTsnr4mZEcUw/ua', 1);
INSERT INTO `t_user` VALUES (6, 'user5', '123456', 1);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `uid` int NOT NULL COMMENT '用户编号',
  `rid` int NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`uid`, `rid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1);
INSERT INTO `t_user_role` VALUES (2, 2);
INSERT INTO `t_user_role` VALUES (3, 4);
INSERT INTO `t_user_role` VALUES (4, 5);

SET FOREIGN_KEY_CHECKS = 1;
