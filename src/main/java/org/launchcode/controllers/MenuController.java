package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;


/**
 * Created by Andre on 4/30/2017.
 */

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    // Request path: index
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";
    }

    // Request path: add GET
    @RequestMapping(value = "add",  method = RequestMethod.GET)
    public String add (Model model) {

        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    // Request path: add POST
    @RequestMapping(value = "add",  method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {

        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu);
        return "redirect:view/"+menu.getId();

    }

    //Request path: viewMenu GET
    @RequestMapping(value="view/{id}", method = RequestMethod.GET)
    public String viewMenu(@PathVariable int id, Model model){
        model.addAttribute("menu",menuDao.findOne(id));

        return "menu/view";

    }

    //Request path: addItem GET
    @RequestMapping(value="add-item/{id}", method = RequestMethod.GET)
    public String addItem (@PathVariable int id, Model model){
        AddMenuItemForm itemForm = new AddMenuItemForm(menuDao.findOne(id), cheeseDao.findAll());


        model.addAttribute("menu",menuDao.findOne(id));
        model.addAttribute("form", itemForm);
        model.addAttribute("title", "Add item to menu:"+menuDao.findOne(id).getName());


        return "menu/add-item";

    }

    //Request path: addItem POST
    @RequestMapping(value="add-item/{id}", method = RequestMethod.POST)
    public String addItem (Model model,@ModelAttribute @Valid AddMenuItemForm menuItemForm, Errors errors){

        if (errors.hasErrors()) {
            model.addAttribute("categories",menuDao.findAll());
            model.addAttribute("form", menuItemForm);
            model.addAttribute("title", "Add item to menu:"+menuDao.findOne(menuItemForm.getMenuId()));
            return "menu/add-item";
        }

        Menu theMenu = menuDao.findOne(menuItemForm.getMenuId());
        Cheese theCheese = cheeseDao.findOne(menuItemForm.getCheeseId());

        theMenu.addItem(theCheese);

        menuDao.save(theMenu);

        return "redirect:../view/" + theMenu.getId();

    }


}
