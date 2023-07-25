import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * @created 24.07.2023 14:58
 */
public class Main {
    private static String pathToAddrObj = "src/main/java/AS_ADDR_OBJ.XML";
    private static String pathToAdmHierarchy = "src/main/java/AS_ADM_HIERARCHY.XML";
    public static void main(String[] args) {



        System.out.println("Задача №1");
        String strDate = "2010-01-01";
        String objectIds = "1422396, 1450759, 1449192, 1451562";

        //Определяем будущий формат данных. Парсим со строки
        LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ArrayList<AddressObject> addrObjs = new ArrayList<>();
        try (XMLReader processor = new XMLReader(Files.newInputStream(Paths.get(pathToAddrObj)))) {
            while (processor.startElement("OBJECT", "ADDRESSOBJECTS")) {
                if (objectIds.contains(processor.getAttribute("OBJECTID"))) {
//                  Создаем объекты, которые потом могут нам потребоваться для обработки
                    AddressObject tmp = new AddressObject(
                            processor.getAttribute("OBJECTID"),
                            processor.getAttribute("TYPENAME"),
                            processor.getAttribute("NAME"),
                            processor.getAttribute("STARTDATE"),
                            processor.getAttribute("ENDDATE"),
                            processor.getAttribute("ISACTUAL"),
                            processor.getAttribute("ISACTIVE")
                    );
//                  Сохраняем их в коллекцию объектов и затем через стримы выводим
                    addrObjs.add(tmp);
                }
            }
            addrObjs.stream().filter(i -> i.isDateBetween(date)).forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        System.out.println("\n\n\nЗадача №2");
        ArrayList<String> ids = new ArrayList<>();
        try (XMLReader processor2 = new XMLReader(Files.newInputStream(Paths.get(pathToAddrObj)))) {
            while (processor2.startElement("OBJECT", "ADDRESSOBJECTS")) {
                //Пробегаемся по всем строчкам, ищем "проезды" и затем ищем родителя до тех пор, пока не найдем АО
                if (processor2.getAttribute("TYPENAME").equals("проезд")) {
                    String tmpId = processor2.getAttribute("OBJECTID");
                    //В среднем цикл срабатывает 3-4 раза.
                    while (!tmpId.equals("0")){
                        ids.add(tmpId);
                        tmpId = findParent(tmpId);
                    }
                    //Для удобства дальнейшего перебора, добавляем |
                    ids.add("|");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //Просто вывод с перебором в обратном порядке
        for (int i = ids.size()-1; i >=0; i--){
            if (ids.get(i).equals("|")){
                System.out.print("\n");
            } else {
                System.out.print(getNameById(ids.get(i)) + ", ");
            }
        }    }

    //метод служит для поиска родителя по id объекта.
    //записи о родителях повторяются, поэтому есть проверка на активный адрес
    public static String findParent(String id){
        try (XMLReader processor = new XMLReader(Files.newInputStream(Paths.get(pathToAdmHierarchy)))) {
            while (processor.startElement("ITEM", "ITEMS")) {
                if (processor.getAttribute("OBJECTID").equals(id) && processor.getAttribute("ISACTIVE").equals("1")){
                    //возвращаем строку с id родителя
                    return processor.getAttribute("PARENTOBJID");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    //метод для удобства вывода. запрашивает тип а затем имя объекта.
    //Также пробегается по всему xml и вытаскивает нужную строку.
    public static String getNameById(String id) {
        try (XMLReader processor2 = new XMLReader(Files.newInputStream(Paths.get(pathToAddrObj)))) {
            while (processor2.startElement("OBJECT", "ADDRESSOBJECTS")) {
                if (processor2.getAttribute("OBJECTID").equals(id)) {
                    return processor2.getAttribute("TYPENAME") + " " + processor2.getAttribute("NAME");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "\n";
    }
}
