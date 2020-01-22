<template>
  <v-container>
    <h2 style="color:#002f6c" class="d-flex justify-center ma-10">
      Bienvenido al Recomendador de Licores
    </h2>
    <div class="justify-center">
    <v-img
            style="width: 50%;display: block; margin-left: auto; margin-right: auto;"
            :src="require('../assets/bar2.jpg')"
    />
    </div>

    <v-data-iterator
            :items="items"
            :items-per-page.sync="itemsPerPage"
            :page="page"
            :search="search"
            :sort-desc="sortDesc"
            hide-default-footer
            style="margin-top: 5%"
    >
      <template v-slot:header>
        <v-toolbar
                dark
                color="blue darken-3"
                class="mb-1"
        >
          <v-text-field
                  v-model="search"
                  clearable
                  flat
                  solo-inverted
                  hide-details
                  label="Buscar"
          />
          <template v-if="$vuetify.breakpoint.mdAndUp">
            <v-spacer></v-spacer>
            <v-spacer/>
            <v-btn-toggle
                    v-model="sortDesc"
                    mandatory
            >
              <v-btn
                      large
                      depressed
                      color="blue"
                      :value="false"
              >
                <v-icon>mdi-arrow-up</v-icon>
              </v-btn>
              <v-btn
                      large
                      depressed
                      color="blue"
                      :value="true"
              >
                <v-icon>mdi-arrow-down</v-icon>
              </v-btn>
            </v-btn-toggle>
          </template>
        </v-toolbar>
      </template>

      <template v-slot:default="props">
        <v-row>
          <v-col
                  v-for="item in props.items"
                  :key="item.title"
                  cols="12"
                  sm="6"
                  md="4"
                  lg="3"
          >
            <v-card>
              <v-card-title class="subheading font-weight-bold">{{ item.title }}</v-card-title>

              <v-divider></v-divider>
              <h4 style="padding: 4%">Categoría: {{item.cateogry}}</h4>
              <h4 style="padding: 4%">Rating: {{item.firtRating}}</h4>
              <div style="padding: 4%">{{item.directions}}</div>
             <!--<h4 style="padding: 4%">Ingredientes:</h4>
              <div style="padding: 4%" v-for="n in item.ingredients"
                   :key="n">
                  {{n[0]}}: {{n[1]}}
              </div>-->
            </v-card>
          </v-col>
        </v-row>
      </template>

      <template v-slot:footer>
        <v-row class="mt-2" align="center" justify="center">
          <span class="grey--text">Artículos por página</span>
          <v-menu offset-y>
            <template v-slot:activator="{ on }">
              <v-btn
                      dark
                      text
                      color="primary"
                      class="ml-2"
                      v-on="on"
              >
                {{ itemsPerPage }}
                <v-icon>mdi-chevron-down</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item
                      v-for="(number, index) in itemsPerPageArray"
                      :key="index"
                      @click="updateItemsPerPage(number)"
              >
                <v-list-item-title>{{ number }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>

          <v-spacer></v-spacer>

          <span
                  class="mr-4
            grey--text"
          >
            Página {{ page }} de {{ numberOfPages }}
          </span>
          <v-btn
                  fab
                  dark
                  color="blue darken-3"
                  class="mr-1"
                  @click="formerPage"
          >
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
          <v-btn
                  fab
                  dark
                  color="blue darken-3"
                  class="ml-1"
                  @click="nextPage"
          >
            <v-icon>mdi-chevron-right</v-icon>
          </v-btn>
        </v-row>
      </template>
    </v-data-iterator>

  </v-container>
</template>

<script>
  import { DrinkResources } from './../endpoints';
export default {
  name: 'HelloWorld',

  data: () => ({
    itemsPerPageArray: [4, 8, 12],
    search: '',
    filter: {},
    sortDesc: false,
    page: 1,
    itemsPerPage: 4,
    sortBy: 'name',
    keys: [
      'Name',
      'Calories',
      'Fat',
      'Carbs',
      'Protein',
      'Sodium',
      'Calcium',
      'Iron',
    ],
    items: [
    ],
  }),
  computed: {
    numberOfPages () {
      return Math.ceil(this.items.length / this.itemsPerPage)
    },
    filteredKeys () {
      return this.keys.filter(key => key !== `Name`)
    },
  },
  mounted() {
    DrinkResources.index().then(data=>{
      this.items = data.data;
      // eslint-disable-next-line no-console
      console.log(data.data)
    })
  },
  methods: {
    nextPage () {
      if (this.page + 1 <= this.numberOfPages) this.page += 1
    },
    formerPage () {
      if (this.page - 1 >= 1) this.page -= 1
    },
    updateItemsPerPage (number) {
      this.itemsPerPage = number
    },
  },
};
</script>
