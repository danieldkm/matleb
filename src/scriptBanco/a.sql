/*
SQLyog Ultimate v9.51 
MySQL - 4.1.22-community-nt : Database - matleb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`matleb` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `matleb`;

/*Data for the table `aluno` */

insert  into `aluno`(`idAluno`,`loginAluno`,`senhaAluno`,`nivel`) values (1,'a','a','Administrador'),(3,'luiz','luiz','Aluno'),(4,'b','b','Aluno'),(5,'c','c','Administrador'),(6,'d','d','Aluno'),(9,'e','e','Aluno'),(10,'f','f','Aluno'),(11,'g','g','Aluno'),(14,'j','j','Aluno'),(15,'k','k','Aluno'),(16,'l','l','Aluno'),(17,'m','m','Aluno'),(18,'n','n','Aluno'),(19,'o','o','Aluno'),(20,'p','p','Aluno'),(21,'q','q','Aluno'),(22,'r','r','Aluno'),(23,'s','s','Aluno'),(24,'t','t','Aluno'),(26,'v','v','Aluno'),(27,'x','x','Aluno'),(28,'y','y','Aluno'),(32,'awd','521545','Aluno'),(33,'a a','a a','Aluno');

/*Data for the table `aluno_and_fases` */

insert  into `aluno_and_fases`(`fkIdAluno`,`fkIdFase`,`score`) values (1,8,1000),(1,9,1000),(1,10,1000),(1,11,924),(1,13,876),(1,14,1000),(3,8,830),(33,8,1000);

/*Data for the table `fase` */

insert  into `fase`(`idFase`,`nomeFase`,`numQuestoes`,`pontQuestao`,`pontMin`,`tempoMax`,`tolerancia`,`desconto`,`aleatorio`) values (8,'Fase 1',11,90,0.5,5,5,5,0),(9,'Fase 2',1,1000,0.5,10,2,5,1),(10,'Fase 3',2,500,0.5,10,2,5,1),(11,'Tabuada 7',12,83,0.8,10,2,8,0),(13,'Tabuada 8',12,83,0.8,10,2,8,0),(14,'Hardcore',35,28,0.95,12,5,4,0);

/*Data for the table `modulo` */

insert  into `modulo`(`idModulo`,`nomeModulo`) values (9,'Jogo 1'),(10,'Tab7-8'),(11,'Hardcore');

/*Data for the table `modulo_has_alunos` */

insert  into `modulo_has_alunos`(`fkIdModulo`,`fkIdAluno`) values (9,6);

/*Data for the table `modulo_has_fases` */

insert  into `modulo_has_fases`(`fkIdModulo`,`fkIdFase`,`sequenciaFase`) values (9,9,1),(9,10,2),(10,11,1),(10,13,2),(11,14,1);

/*Data for the table `questao` */

insert  into `questao`(`idQuestao`,`nomeQuestao`,`respostaQuestao`,`fkIdFase`,`sequenciaQuestao`) values (57,'1 x 0','0',8,0),(58,'1 x 1','1',8,1),(59,'1 x 2','2',8,2),(60,'1 x 3','3',8,3),(61,'1 x 4','4',8,4),(62,'1 x 5','5',8,5),(63,'1 x 6','6',8,6),(64,'1 x 7','7',8,7),(66,'1 x 9','9',8,9),(74,'1 x 10','10',8,10),(75,'1 x 11','11',8,10),(77,'5 x 5','25',9,1),(78,'3 x 3','9',10,0),(79,'6 x 6','36',10,1),(80,'7 x 1','7',11,0),(81,'7 x 2','14',11,1),(82,'7 x 3','21',11,2),(83,'7 x 4','28',11,3),(84,'7 x 5','35',11,4),(85,'7 x 6','42',11,5),(86,'7 x 7','49',11,6),(87,'7 x 8','56',11,7),(88,'7 x 9','63',11,8),(89,'7 x 10','70',11,9),(90,'7 x 11','77',11,10),(91,'7 x 12','84',11,11),(92,'8 x 1','8',13,0),(93,'8 x 2','16',13,1),(94,'8 x 3','24',13,2),(95,'8 x 4','32',13,3),(96,'8 x 5','40',13,4),(97,'8 x 6','48',13,5),(98,'8 x 7','56',13,6),(99,'8 x 8','64',13,7),(100,'8 x 9','72',13,8),(101,'8 x 10','80',13,9),(102,'8 x 11','88',13,10),(103,'8 x 12','96',13,11),(104,'7 x 3','21',14,0),(105,'7 x 4','28',14,1),(106,'7 x 5','35',14,2),(107,'7 x 6','42',14,3),(108,'7 x 7','49',14,4),(109,'7 x 8','56',14,5),(110,'7 x 9','63',14,6),(111,'7 x 10','70',14,7),(112,'7 x 11','77',14,8),(113,'7 x 12','84',14,9),(114,'7 x 13','91',14,10),(115,'8 x 3','24',14,11),(116,'8 x 4','32',14,12),(117,'8 x 5','40',14,13),(118,'8 x 6','48',14,14),(119,'8 x 7','56',14,15),(120,'8 x 8','64',14,16),(121,'8 x 9','72',14,17),(122,'8 x 10','80',14,18),(123,'8 x 11','88',14,19),(124,'8 x 12','96',14,20),(125,'8 x 13','104',14,21),(126,'9 x 3','27',14,22),(127,'9 x 4','36',14,23),(128,'9 x 5','45',14,24),(129,'9 x 6','54',14,25),(130,'9 x 7','63',14,26),(131,'9 x 8','72',14,27),(132,'9 x 9','81',14,28),(133,'9 x 10','90',14,29),(134,'9 x 11','99',14,30),(135,'9 x 12','108',14,31),(136,'9 x 13','117',14,32),(137,'512 ÷ 8','64',14,33),(138,'1024 ÷ 2','512',14,34);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
