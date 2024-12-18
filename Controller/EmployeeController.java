package Controller;

import DAO.EmployeeDAOI;
import DAO.EmployeeDAOImpl;
import Model.Employee;
import Model.Poste;
import Model.Role;
import View.EmployeeView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;


public class EmployeeController {
    private final EmployeeView view;
    private final EmployeeDAOI dao;

    public EmployeeController(EmployeeView view) {
        this.view = view;
        this.dao = new EmployeeDAOImpl();

        // Écouteur pour le bouton Ajouter
        view.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        // Écouteur pour le bouton Lister
        view.listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listEmployees();
            }
        });

        // Écouteur pour le bouton Supprimer
        view.deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        // Écouteur pour le bouton Modifier
        view.modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyEmployee();
            }
        });
    }

    // Méthode pour ajouter un employé
    private void addEmployee() {
        try {
            String nom = view.nameField.getText();
            String prenom = view.surnameField.getText();
            String email = view.emailField.getText();
            String phone = view.phoneField.getText();
            double salaire = Double.parseDouble(view.salaryField.getText());
            Role role = Role.valueOf(view.roleCombo.getSelectedItem().toString().toUpperCase());
            Poste poste = Poste.valueOf(view.posteCombo.getSelectedItem().toString().toUpperCase());

            Employee employee = new Employee(nom, prenom, email, phone, salaire, role, poste);
            dao.add(employee);
            JOptionPane.showMessageDialog(view, "Employé ajouté avec succès.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        }
    }

    // Méthode pour afficher la liste des employés
    private void listEmployees() {
        List<Employee> employees = dao.listAll();
        String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Salaire", "Rôle", "Poste"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Employee emp : employees) {
            Object[] row = {emp.getId(), emp.getNom(), emp.getPrenom(), emp.getEmail(), emp.getPhone(), emp.getSalaire(), emp.getRole(), emp.getPoste()};
            model.addRow(row);
        }

        view.employeeTable.setModel(model);
    }

    // Méthode pour supprimer un employé
    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(view, "Entrez l'ID de l'employé à supprimer :"));
            dao.delete(id);
            JOptionPane.showMessageDialog(view, "Employé supprimé avec succès.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        }
    }

    // Méthode pour modifier un employé
    private void modifyEmployee() {
        try {
            // Demander l'ID de l'employé
            String idInput = JOptionPane.showInputDialog(view, "Entrez l'ID de l'employé à modifier :");
            if (idInput == null || idInput.trim().isEmpty()) {
                throw new IllegalArgumentException("L'ID de l'employé ne peut pas être vide.");
            }
            int id = Integer.parseInt(idInput);

            // Récupérer les données de l'employé à partir des champs
            String nom = view.nameField.getText().trim();
            String prenom = view.surnameField.getText().trim();
            String email = view.emailField.getText().trim();
            String phone = view.phoneField.getText().trim();
            String salaireText = view.salaryField.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || phone.isEmpty() || salaireText.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis.");
            }

            // Valider le salaire
            double salaire = 0;
            try {
                salaire = Double.parseDouble(salaireText);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Le salaire doit être un nombre valide.");
            }

            // Valider le rôle et le poste
            Role role = Role.valueOf(view.roleCombo.getSelectedItem().toString().toUpperCase());
            Poste poste = Poste.valueOf(view.posteCombo.getSelectedItem().toString().toUpperCase());

            // Créer l'employé mis à jour
            Employee updatedEmployee = new Employee(nom, prenom, email, phone, salaire, role, poste);

            // Mettre à jour l'employé dans la base de données
            dao.update(updatedEmployee, id);

            // Afficher un message de succès
            JOptionPane.showMessageDialog(view, "Employé mis à jour avec succès.");

            // Actualiser la liste des employés
            listEmployees();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur inattendue: " + ex.getMessage());
        }
    }

}


