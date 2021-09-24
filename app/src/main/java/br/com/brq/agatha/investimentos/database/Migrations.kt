package br.com.brq.agatha.investimentos.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migrations {
    companion object {
        val migration_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // PASSOS PARA DELETAR COLUNA
                // CRIAR NOVA TABELA
                database.execSQL("CREATE TABLE IF NOT EXISTS `Usuario_novo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `saldoDisponivel` TEXT NOT NULL)")

                // COPIAR DADOS DA TABELA ANTIGA PARA A NOVA
                database.execSQL("INSERT INTO Usuario_novo (id,saldoDisponivel) SELECT id,saldoDisponivel FROM Usuario")

                // REMOVER TABELA ANTIGA
                database.execSQL("DROP TABLE Usuario")

                //RENOMEAR TABELA NOVA
                database.execSQL("ALTER TABLE Usuario_novo RENAME TO Usuario")
            }
        }

        val migration_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `Moeda_novo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `buy` TEXT, `sell` TEXT, `variation` TEXT NOT NULL, `abreviacao` TEXT)")
                database.execSQL("INSERT INTO Moeda_novo (id,name,buy,sell,variation,abreviacao) SELECT id,name,buy,sell,variation,abreviacao FROM Moeda")
                database.execSQL("DROP TABLE Moeda")
                database.execSQL("ALTER TABLE Moeda_novo RENAME TO Moeda")
            }
        }
    }
}